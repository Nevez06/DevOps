package br.com.devops.devops.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.devops.devops.service.AlunoService;
import br.com.devops.devops.service.CursoService;
import br.com.devops.devops.service.DisciplinaService;
import br.com.devops.devops.service.ProfessorService;
import br.com.devops.devops.service.UsuarioService;

@Controller
public class PaginaController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("totalAlunos", alunoService.getAllAlunos().size());
        model.addAttribute("totalCursos", cursoService.listarTodos().size());
        model.addAttribute("totalProfessores", professorService.listarTodos().size());
        model.addAttribute("totalDisciplinas", disciplinaService.listarTodas().size());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());

        String nomeUsuario = "Usuário";
        if (authentication != null && authentication.isAuthenticated()) {
            String login = authentication.getName();
            nomeUsuario = usuarioService.buscarPorLogin(login)
                    .map(usuario -> usuario.getNomeUsuario())
                    .orElse(login);
        }

        model.addAttribute("nomeUsuario", nomeUsuario);
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("pt", "BR")));
        model.addAttribute("dataAtualFormatada", dataFormatada);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
