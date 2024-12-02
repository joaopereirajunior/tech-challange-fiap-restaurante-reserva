package br.com.fiap.restaurante.usecase.restaurante.impl;

import org.springframework.stereotype.Service;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.usecase.restaurante.CriarRestauranteUseCase;

@Service
public class CriarRestauranteUseCaseImpl implements CriarRestauranteUseCase {

	private final RestauranteGateway restauranteGateway;
	
    public CriarRestauranteUseCaseImpl(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
	@Override
	public Restaurante execute(Restaurante restaurante) {
		return restauranteGateway.salvar(restaurante);
	}

}
