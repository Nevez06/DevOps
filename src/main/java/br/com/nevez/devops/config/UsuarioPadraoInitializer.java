package br.com.nevez.devops.config;

import br.com.nevez.devops.entity.Usuario;
import br.com.nevez.devops.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Contas iniciais desativadas por padrão: nunca publique senhas de demonstração.
 */
@Component
@ConditionalOnProperty(name = "app.bootstrap.enabled", havingValue = "true")
public class UsuarioPadraoInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final String senhaAdmin;
    private final String senhaProfessor;
    private final String senhaSecretaria;

    public UsuarioPadraoInitializer(
            UsuarioRepository usuarios,
            PasswordEncoder encoder,
            @Value("${app.bootstrap.admin-password:}") String senhaAdmin,
            @Value("${app.bootstrap.professor-password:}") String senhaProfessor,
            @Value("${app.bootstrap.secretaria-password:}") String senhaSecretaria) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.senhaAdmin = senhaAdmin;
        this.senhaProfessor = senhaProfessor;
        this.senhaSecretaria = senhaSecretaria;
    }

    @Override
    public void run(String... args) {
        if (usuarios.count() != 0) return;
        if (senhaAdmin == null || senhaAdmin.isBlank() || senhaAdmin.length() < 8) {
            throw new IllegalStateException("Defina APP_ADMIN_PASSWORD com pelo menos 8 caracteres para inicializar o projeto.");
        }
        criar("Administrador", "admin@devops.com", "admin", senhaAdmin, "ADMIN");
        if (senhaProfessor != null && !senhaProfessor.isBlank()) {
            criar("João Silva", "joao@devops.com", "joao", senhaProfessor, "PROFESSOR");
        }
        if (senhaSecretaria != null && !senhaSecretaria.isBlank()) {
            criar("Maria Santos", "maria@devops.com", "maria", senhaSecretaria, "SECRETARIA");
        }
    }

    private void criar(String nome, String email, String login, String senha, String perfil) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(nome);
        usuario.setEmailUsuario(email);
        usuario.setLoginUsuario(login);
        usuario.setSenhaUsuario(encoder.encode(senha));
        usuario.setRoleUsuario(perfil);
        usuarios.save(usuario);
    }
}
