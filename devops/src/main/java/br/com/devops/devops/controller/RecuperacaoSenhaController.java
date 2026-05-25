package br.com.devops.devops.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.devops.devops.entity.Usuario;
import br.com.devops.devops.service.EmailService;
import br.com.devops.devops.service.UsuarioService;

@Controller
public class RecuperacaoSenhaController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public RecuperacaoSenhaController(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @GetMapping("/recuperar-senha")
    public String solicitarRecuperacao() {
        return "recuperar-senha";
    }

    @PostMapping("/recuperar-senha")
    public String enviarRecuperacao(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email.trim());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = usuarioService.criarTokenRecuperacao(usuario);
            String link = baseUrl + "/redefinir-senha?token=" + token;

            if (emailService.envioConfigurado()) {
                emailService.enviarRecuperacaoSenha(usuario.getEmailUsuario(), usuario.getNomeUsuario(), link);
            } else {
                redirectAttributes.addFlashAttribute("linkDev", link);
            }
        }

        redirectAttributes.addFlashAttribute("mensagem",
                "Se o email estiver cadastrado, enviaremos um link de recuperação com validade de 30 minutos.");
        return "redirect:/recuperar-senha";
    }

    @GetMapping("/redefinir-senha")
    public String formularioRedefinicao(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorTokenRecuperacao(token);
        if (usuarioOpt.isEmpty() || !usuarioService.tokenValido(usuarioOpt.get())) {
            redirectAttributes.addFlashAttribute("erro", "Link inválido ou expirado. Solicite uma nova recuperação.");
            return "redirect:/recuperar-senha";
        }
        model.addAttribute("token", token);
        return "redefinir-senha";
    }

    @PostMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam String token, @RequestParam String senha,
            @RequestParam String confirmarSenha, RedirectAttributes redirectAttributes, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorTokenRecuperacao(token);
        if (usuarioOpt.isEmpty() || !usuarioService.tokenValido(usuarioOpt.get())) {
            redirectAttributes.addFlashAttribute("erro", "Link inválido ou expirado. Solicite uma nova recuperação.");
            return "redirect:/recuperar-senha";
        }
        if (senha == null || senha.length() < 6) {
            model.addAttribute("token", token);
            model.addAttribute("erro", "A senha precisa ter pelo menos 6 caracteres.");
            return "redefinir-senha";
        }
        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("token", token);
            model.addAttribute("erro", "As senhas não conferem.");
            return "redefinir-senha";
        }

        usuarioService.redefinirSenha(usuarioOpt.get(), senha);
        redirectAttributes.addFlashAttribute("mensagem", "Senha redefinida com sucesso. Faça login com a nova senha.");
        return "redirect:/login";
    }
}
