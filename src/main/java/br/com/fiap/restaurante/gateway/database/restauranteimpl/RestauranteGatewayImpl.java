package br.com.fiap.restaurante.gateway.database.restauranteimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.database.entity.restaurante.RestauranteEntity;
import br.com.fiap.restaurante.gateway.database.repository.restaurante.RestauranteRepository;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

@Repository
public class RestauranteGatewayImpl implements RestauranteGateway {

	private final RestauranteRepository restauranteRepository;
	
    public RestauranteGatewayImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }
    
    @Override
    public Restaurante salvar(Restaurante restaurante) {
    	RestauranteEntity novoRestaurante = mapToEntity(restaurante);
    	
        RestauranteEntity restauranteSalvo = restauranteRepository.save(novoRestaurante);
        return new Restaurante(restauranteSalvo.getId(), restauranteSalvo.getNome(), restauranteSalvo.getLocalizacao(),
        		restauranteSalvo.getTipoCozinha(), restauranteSalvo.getHorarioFuncionamento(), restauranteSalvo.getCapacidade());

    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .map(entity -> new Restaurante(entity.getId(), entity.getNome(), entity.getLocalizacao(),
                		entity.getTipoCozinha(), entity.getHorarioFuncionamento(), entity.getCapacidade()));
    }

    @Override
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll().stream()
                .map(entity -> new Restaurante(entity.getId(), entity.getNome(), entity.getLocalizacao(),
                		entity.getTipoCozinha(), entity.getHorarioFuncionamento(), entity.getCapacidade()))
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
    	restauranteRepository.deleteById(id);
    }
    
    @Override
    public Restaurante atualizar(Restaurante restaurante) {
    	RestauranteEntity entity = mapToEntity(restaurante);
    	
    	return mapToDomain(restauranteRepository.save(entity));
    }
    
	private RestauranteEntity mapToEntity(Restaurante restaurante) {
		return new RestauranteEntity(restaurante.getId(), restaurante.getNome(), restaurante.getLocalizacao(),
				restaurante.getTipoCozinha(), restaurante.getHorarioFuncionamento(), restaurante.getCapacidade());
	}
	
	private Restaurante mapToDomain(RestauranteEntity restauranteEntity) {
		return new Restaurante(restauranteEntity.getId(), restauranteEntity.getNome(), restauranteEntity.getLocalizacao(),
				restauranteEntity.getTipoCozinha(), restauranteEntity.getHorarioFuncionamento(), restauranteEntity.getCapacidade());
		
		
	}
}
