package br.com.fiap.restaurante.usecase.restaurante;

import java.util.Optional;

import br.com.fiap.restaurante.domain.Restaurante;

public interface ObterRestaurantePorIdUseCase {
	Optional<Restaurante> execute(Long id);
}
