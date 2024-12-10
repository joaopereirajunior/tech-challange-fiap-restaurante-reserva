package br.com.fiap.restaurante.exception;

public class ReservaNaoEncontradaException extends RuntimeException{

    public ReservaNaoEncontradaException (Long id) {
        this("Reserva não encontrada com o ID: " + id);
    }

    public ReservaNaoEncontradaException(String message) {
        super(message);
    }
}