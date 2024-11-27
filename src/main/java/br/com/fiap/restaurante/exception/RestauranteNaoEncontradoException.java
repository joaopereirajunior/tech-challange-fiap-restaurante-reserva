package br.com.fiap.restaurante.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {
	public RestauranteNaoEncontradoException(String message) {
		super(message);
	}
}
