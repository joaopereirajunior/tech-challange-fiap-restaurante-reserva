package br.com.fiap.restaurante.usecase.restaurante.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.usecase.restaurante.ObterRestaurantePorIdUseCase;

@Service
public class ObterRestaurantePorIdUseCaseImpl implements ObterRestaurantePorIdUseCase {

	private final RestauranteGateway restauranteGateway;
	
    public ObterRestaurantePorIdUseCaseImpl(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
	
	@Override
	public Optional<Restaurante> execute(Long id) {
		
		return restauranteGateway.buscarPorId(id);
	}

}
