package br.com.devops.devops.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${app.mail.from:}")
    private String mailFrom;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean envioConfigurado() {
        return StringUtils.hasText(mailUsername);
    }

    public void enviarRecuperacaoSenha(String destino, String nome, String link) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destino);
        if (StringUtils.hasText(mailFrom)) {
            mensagem.setFrom(mailFrom);
        }
        mensagem.setSubject("Recuperação de senha - Sistema DevOps");
        mensagem.setText("Olá, " + nome + "!\n\n"
                + "Recebemos uma solicitação para redefinir sua senha. Clique no link abaixo para criar uma nova senha:\n"
                + link + "\n\n"
                + "Este link expira em 30 minutos. Se você não solicitou a recuperação, ignore este email.\n\n"
                + "Sistema DevOps");
        mailSender.send(mensagem);
    }
}
