package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.ObterClientePorIdUseCase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObterClientePorIdUseCaseImpl implements ObterClientePorIdUseCase {

    private final ClienteGateway clienteGateway;

    public ObterClientePorIdUseCaseImpl(ClienteGateway clienteGateway)
    {
        this.clienteGateway = clienteGateway;
    }
    @Override
    public Optional<Cliente> execute(Long id)
    {
        return clienteGateway.buscarPorId(id);
    }
}
