package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
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
class ObterRestaurantePorIdUseCaseImplIT {

	@Autowired
	private ObterRestaurantePorIdUseCaseImpl obterRestaurantePorIdUseCaseImpl;

	@Autowired
	RestauranteGateway restauranteGateway;


	@BeforeEach
	void setup(){

	}


	@Test
	void devePermitirObterUmRestaurantePeloId() {
		// Arrange
		Restaurante restaurante = registrarRestaurante();

		// Act
		Restaurante restauranteObtido = obterRestaurantePorIdUseCaseImpl.execute(restaurante.getId());

		// Assert
		assertThat(restauranteObtido).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restauranteObtido.getNome()).isEqualTo(restaurante.getNome());
		assertThat(restauranteObtido.getLocalizacao()).isEqualTo(restaurante.getLocalizacao());
		assertThat(restauranteObtido.getTipoCozinha()).isEqualTo(restaurante.getTipoCozinha());
		assertThat(restauranteObtido.getHorarioFuncionamento()).isEqualTo(restaurante.getHorarioFuncionamento());
		assertThat(restauranteObtido.getCapacidade()).isEqualTo(restaurante.getCapacidade());
	}

	@Test
	void deveGerarExceptionAoObterUmRestaurante_Restaurante_Nao_Existe() {
		// Arrange
		Long idInexistente = 113123L;


		// Act & Assert
		assertThatThrownBy(() -> obterRestaurantePorIdUseCaseImpl.execute(idInexistente))
				.isInstanceOf(RestauranteNaoEncontradoException.class)
				.hasMessage("O restaurante informado não existe.");

	}

	private Restaurante gerarRestaurante() {
		return new Restaurante(null, "Heroe's Burguer",
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}

	private Restaurante registrarRestaurante() {
		var restaurante = gerarRestaurante();
		var restauranteObtido = restauranteGateway.salvar(restaurante);

		return restauranteObtido;
	}
}
