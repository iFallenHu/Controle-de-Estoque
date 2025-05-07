package br.com.techsolucoes.ControleEstoque.exception;

public class CategoriaNotFoundException extends  RuntimeException{

    public CategoriaNotFoundException(String messsage){
        super(messsage);
    }
}
