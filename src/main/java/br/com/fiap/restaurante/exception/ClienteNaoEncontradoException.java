package br.com.fiap.restaurante.exception;

public class ClienteNaoEncontradoException extends RuntimeException{
    public ClienteNaoEncontradoException(String message) {
        super(message);
    }

    public ClienteNaoEncontradoException(Long id) {
        this("Client não encontrado com ID: " + id);
    }
}
