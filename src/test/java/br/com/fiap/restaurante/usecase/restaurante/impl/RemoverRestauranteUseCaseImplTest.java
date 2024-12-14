package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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

class RemoverRestauranteUseCaseImplTest {

	private RemoverRestauranteUseCaseImpl removerRestauranteUseCaseImpl;

	@Mock
	RestauranteGateway restauranteGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
		removerRestauranteUseCaseImpl = new RemoverRestauranteUseCaseImpl(restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}

	@Test
	void devePermitirRemoverUmRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		Long id = restaurante.getId();
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));
		doNothing().when(restauranteGateway).deletar(id);
		
		// Act
		removerRestauranteUseCaseImpl.execute(id);
		
		// Assert
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
		verify(restauranteGateway, times(1)).deletar(anyLong());
	}
	
	@Test
	void deveGerarExceptionAoRemoverUmRestaurante_Restaurante_Nao_Existe() {
		Long idInexistente = 414129L;
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThatThrownBy(() -> removerRestauranteUseCaseImpl.execute(idInexistente))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("O restaurante informado não existe.");
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
		verify(restauranteGateway, never()).deletar(anyLong());
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}