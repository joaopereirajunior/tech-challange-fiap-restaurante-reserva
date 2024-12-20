package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.CriarClienteUseCase;
import org.springframework.stereotype.Service;

@Service
public class CriarClienteUseCaseImpl implements CriarClienteUseCase {

    private final ClienteGateway clienteGateway;
    public CriarClienteUseCaseImpl(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }


    @Override
    public Cliente execute(Cliente cliente)
    {
        return clienteGateway.salvar(cliente);
    }
}
