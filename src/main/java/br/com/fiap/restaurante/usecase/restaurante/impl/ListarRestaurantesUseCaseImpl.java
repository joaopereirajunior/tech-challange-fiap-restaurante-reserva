package br.com.fiap.restaurante.usecase.restaurante.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.usecase.restaurante.ListarRestaurantesUseCase;

@Service
public class ListarRestaurantesUseCaseImpl implements ListarRestaurantesUseCase {

	private final RestauranteGateway restauranteGateway;
	
    public ListarRestaurantesUseCaseImpl(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
	@Override
	public List<Restaurante> execute() {
		return restauranteGateway.listarTodos();
	}

}
