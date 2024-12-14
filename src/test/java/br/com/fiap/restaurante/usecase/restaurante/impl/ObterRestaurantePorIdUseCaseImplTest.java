package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class ObterRestaurantePorIdUseCaseImplTest {
	
	private ObterRestaurantePorIdUseCaseImpl obterRestaurantePorIdUseCaseImpl;
	
	@Mock
	RestauranteGateway restauranteGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		obterRestaurantePorIdUseCaseImpl = new ObterRestaurantePorIdUseCaseImpl(restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirObterUmRestaurantePeloId() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		Long id = restaurante.getId();
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));
		
		// Act
		Restaurante restauranteObtido = obterRestaurantePorIdUseCaseImpl.execute(id);
		
		// Assert
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
		assertThat(restauranteObtido).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restauranteObtido.getId()).isEqualTo(restaurante.getId());
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
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThatThrownBy(() -> obterRestaurantePorIdUseCaseImpl.execute(idInexistente))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("O restaurante informado não existe.");
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}
