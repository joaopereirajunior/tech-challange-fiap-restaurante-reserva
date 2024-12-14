package br.com.fiap.restaurante.usecase.restaurante.impl;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class RemoverRestauranteUseCaseImplIT {

	@Autowired
	private RemoverRestauranteUseCaseImpl removerRestauranteUseCaseImpl;

	@Autowired
	RestauranteGateway restauranteGateway;

	@BeforeEach
	void setup() {

	}


	@Test
	void devePermitirRemoverUmRestaurante() {
		//Arrange
		var restaurante = registrarRestaurante();
		//Act
		removerRestauranteUseCaseImpl.execute(restaurante.getId());
		//Assert
		assertThatThrownBy(() -> removerRestauranteUseCaseImpl.execute(restaurante.getId()))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("O restaurante informado não existe.");
	}
	
	@Test
	void deveGerarExceptionAoRemoverUmRestaurante_Restaurante_Nao_Existe() {
		Long idInexistente = 414129L;

		// Act & Assert
		assertThatThrownBy(() -> removerRestauranteUseCaseImpl.execute(idInexistente))
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