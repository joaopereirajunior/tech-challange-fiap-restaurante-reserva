package br.com.fiap.restaurante.usecase.restaurante;

import br.com.fiap.restaurante.domain.Restaurante;

public interface ObterRestaurantePorIdUseCase {
	Restaurante execute(Long id);
}
