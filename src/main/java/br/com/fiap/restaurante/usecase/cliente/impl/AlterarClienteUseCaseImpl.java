package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.exception.ClienteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.AlterarClienteUseCase;
import org.springframework.stereotype.Service;

@Service
public class AlterarClienteUseCaseImpl implements AlterarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public AlterarClienteUseCaseImpl(ClienteGateway clienteGateway){
        this.clienteGateway = clienteGateway;
    }
    @Override
    public Cliente execute(Long id, Cliente cliente) {
        cliente.setId(id);
        if(id == null) {
            throw new IllegalArgumentException("ID cliente não pode ser nulo");
        }
        Cliente clienteExistente = clienteGateway.buscarPorId(id)
                .orElseThrow(() -> {
                    throw new ClienteNaoEncontradoException("Cliente não encontrado");
                });
        return clienteGateway.salvar(cliente);
    }
}
