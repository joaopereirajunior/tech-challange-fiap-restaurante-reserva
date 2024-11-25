package br.com.fiap.restaurante.usecase.cliente;

import br.com.fiap.restaurante.domain.Cliente;

public interface CriarClienteUseCase {
    Cliente execute (Cliente cliente);
}
