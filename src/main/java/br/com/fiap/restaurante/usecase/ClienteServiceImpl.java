package br.com.fiap.restaurante.usecase;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.exception.ClienteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.ClienteGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteGateway clienteGateway;

    public ClienteServiceImpl(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    @Override
    public Cliente criarCliente(Cliente cliente) {
        return clienteGateway.salvar(cliente);
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        return clienteGateway.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteGateway.listarTodos();
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente cliente) {
        buscarClientePorId(id); // Verifica se existe
        cliente.setId(id);
        return clienteGateway.salvar(cliente);
    }

    @Override
    public void deletarCliente(Long id) {
        buscarClientePorId(id);
        clienteGateway.deletar(id);
    }
}
