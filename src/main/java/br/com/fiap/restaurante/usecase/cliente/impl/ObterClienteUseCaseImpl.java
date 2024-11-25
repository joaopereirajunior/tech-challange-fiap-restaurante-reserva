package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.ObterClienteUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterClienteUseCaseImpl implements ObterClienteUseCase {

    private final ClienteGateway clienteGateway;

    public ObterClienteUseCaseImpl(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }
        @Override
    public List<Cliente> execute()
    {
        return clienteGateway.listarTodos();

    }
}
