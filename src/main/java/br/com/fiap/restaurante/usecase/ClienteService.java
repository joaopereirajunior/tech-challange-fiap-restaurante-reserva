package br.com.fiap.restaurante.usecase;

import br.com.fiap.restaurante.domain.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente criarCliente(Cliente cliente);

    Cliente buscarClientePorId(Long id);

    List<Cliente> listarClientes();

    Cliente atualizarCliente(Long id, Cliente cliente);

    void deletarCliente(Long id);
}
