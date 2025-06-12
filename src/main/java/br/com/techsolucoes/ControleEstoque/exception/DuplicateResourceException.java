package br.com.techsolucoes.ControleEstoque.exception;

public class DuplicateResourceException extends RuntimeException{

    public DuplicateResourceException (String mensagem){
        super(mensagem);
    }
}
