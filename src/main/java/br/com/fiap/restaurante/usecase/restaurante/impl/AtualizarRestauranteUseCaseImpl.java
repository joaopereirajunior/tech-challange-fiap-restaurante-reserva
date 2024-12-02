package br.com.fiap.restaurante.usecase.restaurante.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.usecase.restaurante.AtualizarRestauranteUseCase;

@Service
public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

	private final RestauranteGateway restauranteGateway;
	
    public AtualizarRestauranteUseCaseImpl(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
	@Override
	public Restaurante execute(Long id, Restaurante restaurante) {
		
		Optional<Restaurante> entity = restauranteGateway.buscarPorId(id);
		
		if (!entity.isPresent()) { 
			throw new RestauranteNaoEncontradoException("O restaurante informado não existe.");
		}

		return restauranteGateway.atualizar(restaurante);
	}
	
}
