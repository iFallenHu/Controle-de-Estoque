package br.com.techsolucoes.ControleEstoque.exception;

public class EmailJaCadastradoException extends  RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado.");
    }
}
