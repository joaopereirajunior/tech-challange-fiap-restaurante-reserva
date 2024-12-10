package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class CriarRestauranteUseCaseImplTest {

	private CriarRestauranteUseCaseImpl criarRestauranteUseCaseImpl;
	
	@Mock
	RestauranteGateway restauranteGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		criarRestauranteUseCaseImpl = new CriarRestauranteUseCaseImpl(restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirCriacaoDeRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		when(restauranteGateway.salvar(any(Restaurante.class))).thenReturn(restaurante);
		
		// Act
		Restaurante mensagemObtida = criarRestauranteUseCaseImpl.execute(restaurante);
		
		// Assert
		verify(restauranteGateway, times(1)).salvar(any(Restaurante.class));
		assertThat(mensagemObtida).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restaurante.getId()).isEqualTo(mensagemObtida.getId());
		assertThat(restaurante.getNome()).isEqualTo(mensagemObtida.getNome());
		assertThat(restaurante.getLocalizacao()).isEqualTo(mensagemObtida.getLocalizacao());
		assertThat(restaurante.getTipoCozinha()).isEqualTo(mensagemObtida.getTipoCozinha());
		assertThat(restaurante.getHorarioFuncionamento()).isEqualTo(mensagemObtida.getHorarioFuncionamento());
		assertThat(restaurante.getCapacidade()).isEqualTo(mensagemObtida.getCapacidade());
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}
