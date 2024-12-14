package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class CriarRestauranteUseCaseImplIT {

	@Autowired
	private CriarRestauranteUseCaseImpl criarRestauranteUseCaseImpl;

	@Autowired
	RestauranteGateway restauranteGateway;

	@BeforeEach
	void setup(){

	}

	@Test
	void devePermitirCriacaoDeRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		
		// Act
		Restaurante mensagemObtida = criarRestauranteUseCaseImpl.execute(restaurante);
		
		// Assert
		assertThat(mensagemObtida).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restaurante.getNome()).isEqualTo(mensagemObtida.getNome());
		assertThat(restaurante.getLocalizacao()).isEqualTo(mensagemObtida.getLocalizacao());
		assertThat(restaurante.getTipoCozinha()).isEqualTo(mensagemObtida.getTipoCozinha());
		assertThat(restaurante.getHorarioFuncionamento()).isEqualTo(mensagemObtida.getHorarioFuncionamento());
		assertThat(restaurante.getCapacidade()).isEqualTo(mensagemObtida.getCapacidade());
	}


	private Restaurante gerarRestaurante() {
		return new Restaurante(null, "Heroe's Burguer",
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}


}
