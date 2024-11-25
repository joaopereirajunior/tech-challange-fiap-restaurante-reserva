package br.com.fiap.restaurante.usecase.cliente;

import br.com.fiap.restaurante.domain.Cliente;

import java.util.List;

public interface ObterClienteUseCase {
    List<Cliente> execute ();
}
