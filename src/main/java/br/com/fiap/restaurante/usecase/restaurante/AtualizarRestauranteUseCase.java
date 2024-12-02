package br.com.fiap.restaurante.usecase.restaurante;

import br.com.fiap.restaurante.domain.Restaurante;

public interface AtualizarRestauranteUseCase {

	Restaurante execute(Long id, Restaurante restaurante);
}
