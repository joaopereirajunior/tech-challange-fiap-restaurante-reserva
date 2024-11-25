package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.ClienteGateway;
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
        System.out.println("criou o cliente aqui +++++++++++++++++++++++++++++++++++");
        return clienteGateway.salvar(cliente);
    }
}
