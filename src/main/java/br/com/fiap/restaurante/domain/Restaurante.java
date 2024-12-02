package br.com.fiap.restaurante.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Restaurante {

	private Long id;
	private String nome;
	private String localizacao;
	private String tipoCozinha;
	private String horarioFuncionamento;
	private int capacidade;
}
