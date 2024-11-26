package br.com.fiap.restaurante.gateway.cliente;

import br.com.fiap.restaurante.domain.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface ClienteRepositoryGateway {
    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> listarTodos();

    void deletar(Long id);

}
