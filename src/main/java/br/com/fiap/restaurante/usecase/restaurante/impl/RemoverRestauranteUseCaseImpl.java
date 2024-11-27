package br.com.fiap.restaurante.usecase.restaurante.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.usecase.restaurante.RemoverRestauranteUseCase;

@Service
public class RemoverRestauranteUseCaseImpl implements RemoverRestauranteUseCase {

	private final RestauranteGateway restauranteGateway;
	
    public RemoverRestauranteUseCaseImpl(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
	@Override
	public void execute(Long id) {
		
		Optional<Restaurante> entity = restauranteGateway.buscarPorId(id);
		
		if (entity == null) { 
			throw new RestauranteNaoEncontradoException("O restaurante informado não existe.");
		}
		
		restauranteGateway.deletar(id);
	}
	
}
