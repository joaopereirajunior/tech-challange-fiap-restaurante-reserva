package br.com.fiap.restaurante.controller.cliente.mapper;

import br.com.fiap.restaurante.controller.cliente.dto.ClienteRequestDTO;
import br.com.fiap.restaurante.domain.Cliente;

public class ClienteMapper {
    public static Cliente toDomain(ClienteRequestDTO dto){
        return new Cliente(null, dto.nome(), dto.cpf());
    }
}
