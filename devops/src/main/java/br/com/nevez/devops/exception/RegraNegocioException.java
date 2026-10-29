package br.com.nevez.devops.exception;

// Erro de validação de regra de negócio, exibido ao usuário como mensagem amigável
public class RegraNegocioException extends IllegalArgumentException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
