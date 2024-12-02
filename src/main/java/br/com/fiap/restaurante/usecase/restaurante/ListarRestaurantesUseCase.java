package br.com.fiap.restaurante.usecase.restaurante;

import java.util.List;

import br.com.fiap.restaurante.domain.Restaurante;

public interface ListarRestaurantesUseCase {
	List<Restaurante> execute();
}
