package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.gateway.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.DeletarClienteUseCase;
import org.springframework.stereotype.Service;

@Service
public class DeletarClienteUseCaseImpl implements DeletarClienteUseCase {
    private final ClienteGateway clienteGateway;

    public DeletarClienteUseCaseImpl(ClienteGateway clienteGateway)
    {
        this.clienteGateway = clienteGateway;
    }

    @Override
    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cliente não pode ser nulo");
        }
        if (!clienteGateway.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        clienteGateway.deletar(id);
    }
}
