package br.com.fiap.restaurante.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
}
