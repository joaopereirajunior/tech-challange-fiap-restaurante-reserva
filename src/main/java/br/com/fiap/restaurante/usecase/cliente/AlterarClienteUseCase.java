package br.com.fiap.restaurante.usecase.cliente;

import br.com.fiap.restaurante.domain.Cliente;

public interface AlterarClienteUseCase {
    public Cliente execute(Long id, Cliente cliente);

}
