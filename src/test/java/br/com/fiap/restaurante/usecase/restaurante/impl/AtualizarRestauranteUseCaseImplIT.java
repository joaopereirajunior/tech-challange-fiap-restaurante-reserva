package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.database.restauranteimpl.RestauranteGatewayImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class AtualizarRestauranteUseCaseImplIT {

	@Autowired
	private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCaseImpl;

    @Autowired
    private RestauranteGatewayImpl restauranteGatewayImpl;

	@Autowired
	private RestauranteGateway restauranteGateway;

	@BeforeEach
	void setup(){
	
	}

	@AfterEach
	void teardown() throws Exception {

	}
	
	@Test
	void devePermitirAtualizacaoDeRestaurante() {
		// Arrange
		Restaurante restaurante = registrarRestaurante();
		Restaurante restauranteModificado = gerarRestaurante();

		restauranteModificado.setId(restaurante.getId());
		restauranteModificado.setLocalizacao("Av. Salvador, 101");
		restauranteModificado.setTipoCozinha("Lanches Diversos");
		restauranteModificado.setCapacidade(30);

		var restauranteOptional = restauranteGatewayImpl.buscarPorId(restaurante.getId());

		if(restauranteOptional.isPresent())
			restaurante = restauranteOptional.get();
		
		// Act
		Restaurante restauranteObtido = atualizarRestauranteUseCaseImpl.execute(restaurante.getId(),
				restauranteModificado);
		
		// Assert
		assertThat(restauranteObtido).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restaurante.getId()).isEqualTo(restauranteObtido.getId());
		assertThat(restaurante.getNome()).isEqualTo(restauranteObtido.getNome());
		assertThat(restaurante.getLocalizacao()).isNotEqualTo(restauranteObtido.getLocalizacao());
		assertThat(restaurante.getTipoCozinha()).isNotEqualTo(restauranteObtido.getTipoCozinha());
		assertThat(restaurante.getHorarioFuncionamento()).isEqualTo(restauranteObtido.getHorarioFuncionamento());
		assertThat(restaurante.getCapacidade()).isNotEqualTo(restauranteObtido.getCapacidade());
	}
	
	@Test
	void deveGerarExceptionAoAtualizarRestaurante_Restaurante_Nao_Existe() {
		// Arrange
		Long idInexistente = 113123L;
		Restaurante restaurante = registrarRestaurante();

		// Act & Assert
		assertThatThrownBy(() -> atualizarRestauranteUseCaseImpl.execute(idInexistente,
				restaurante))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("O restaurante informado não existe.");

	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(null, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}

	private Restaurante registrarRestaurante() {
		var restaurante = gerarRestaurante();

		return restauranteGateway.salvar(restaurante);
	}

}
