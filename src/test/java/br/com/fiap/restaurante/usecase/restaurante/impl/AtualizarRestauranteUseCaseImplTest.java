package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

class AtualizarRestauranteUseCaseImplTest {

	private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCaseImpl;
	
	@Mock
	RestauranteGateway restauranteGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		atualizarRestauranteUseCaseImpl = new AtualizarRestauranteUseCaseImpl(restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirAtualizacaoDeRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		Restaurante restauranteModificado = gerarRestaurante();
		restauranteModificado.setLocalizacao("Av. Salvador, 101");
		restauranteModificado.setTipoCozinha("Lanches Diversos");
		restauranteModificado.setCapacidade(30);
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.of(restaurante));
		when(restauranteGateway.atualizar(any(Restaurante.class))).thenReturn((restauranteModificado));
		
		// Act
		Restaurante mensagemObtida = atualizarRestauranteUseCaseImpl.execute(restaurante.getId(),
				restauranteModificado);
		
		// Assert
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
		verify(restauranteGateway, times(1)).atualizar(any(Restaurante.class));
		assertThat(mensagemObtida).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(restaurante.getId()).isEqualTo(mensagemObtida.getId());
		assertThat(restaurante.getNome()).isEqualTo(mensagemObtida.getNome());
		assertThat(restaurante.getLocalizacao()).isNotEqualTo(mensagemObtida.getLocalizacao());
		assertThat(restaurante.getTipoCozinha()).isNotEqualTo(mensagemObtida.getTipoCozinha());
		assertThat(restaurante.getHorarioFuncionamento()).isEqualTo(mensagemObtida.getHorarioFuncionamento());
		assertThat(restaurante.getCapacidade()).isNotEqualTo(mensagemObtida.getCapacidade());
	}
	
	@Test
	void deveGerarExceptionAoAtualizarRestaurante_Restaurante_Nao_Existe() {
		// Arrange
		Long idInexistente = 113123L;
		Restaurante restaurante = gerarRestaurante();
		when(restauranteGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThatThrownBy(() -> atualizarRestauranteUseCaseImpl.execute(idInexistente,
				restaurante))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("O restaurante informado não existe.");
		verify(restauranteGateway, times(1)).buscarPorId(anyLong());
		verify(restauranteGateway, never()).atualizar(any(Restaurante.class));

	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
}
