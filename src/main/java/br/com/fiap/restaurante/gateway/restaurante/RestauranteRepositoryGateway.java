package br.com.fiap.restaurante.gateway.restaurante;

import java.util.List;
import java.util.Optional;

import br.com.fiap.restaurante.domain.Restaurante;

public interface RestauranteRepositoryGateway {

	Restaurante salvar(Restaurante restaurante);

	Optional<Restaurante> buscarPorId(Long id);

	List<Restaurante> listarTodos();

	void deletar(Long id);
	
	Restaurante atualizar(Restaurante restaurante);

}
