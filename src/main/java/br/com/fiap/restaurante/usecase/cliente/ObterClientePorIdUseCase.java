package br.com.fiap.restaurante.usecase.cliente;

import br.com.fiap.restaurante.domain.Cliente;

import java.util.Optional;

public interface ObterClientePorIdUseCase {
    Optional<Cliente> execute(Long id);
}
