package br.com.fiap.restaurante.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {
	public RestauranteNaoEncontradoException(String message) {
		super(message);
	}

	public RestauranteNaoEncontradoException(Long id) {
        this("Restaurante não encontrado com ID: " + id);
    }
}
