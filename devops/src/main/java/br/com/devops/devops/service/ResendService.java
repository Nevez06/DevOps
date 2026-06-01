package br.com.devops.devops.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ResendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResendService.class);
    private static final String DEFAULT_API_URL = "https://api.resend.com/emails";
    private static final String DEFAULT_FROM = "onboarding@resend.dev";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.api.url:" + DEFAULT_API_URL + "}")
    private String resendApiUrl;

    public boolean enviarRecuperacaoSenha(String destino, String nome, String link) {
        if (!StringUtils.hasText(resendApiKey)) {
            LOGGER.error("Falha ao enviar email de recuperação: propriedade resend.api.key não configurada.");
            return false;
        }

        String payload = montarPayload(destino, nome, link);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(resendApiUrl))
                .header("Authorization", "Bearer " + resendApiKey.trim())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return true;
            }

            LOGGER.error("Resend retornou erro ao enviar email de recuperação. status={}, body={}",
                    response.statusCode(), response.body());
            return false;
        } catch (IOException e) {
            LOGGER.error("Erro de I/O ao enviar email de recuperação via Resend.", e);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Envio de email de recuperação via Resend foi interrompido.", e);
            return false;
        } catch (RuntimeException e) {
            LOGGER.error("Erro inesperado ao enviar email de recuperação via Resend.", e);
            return false;
        }
    }

    private String montarPayload(String destino, String nome, String link) {
        String assunto = "Recuperação de senha - Sistema DevOps";
        String html = "<p>Olá, " + escaparHtml(nome) + "!</p>"
                + "<p>Recebemos uma solicitação para redefinir sua senha.</p>"
                + "<p><a href=\"" + link + "\">Clique aqui para redefinir sua senha</a></p>"
                + "<p>Este link expira em 30 minutos. Se você não solicitou a recuperação, ignore este email.</p>"
                + "<p>Sistema DevOps</p>";

        return """
                {
                  "from": "%s",
                  "to": ["%s"],
                  "subject": "%s",
                  "html": "%s"
                }
                """.formatted(
                escaparJson(DEFAULT_FROM),
                escaparJson(destino),
                escaparJson(assunto),
                escaparJson(html));
    }

    private String escaparJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String escaparHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
