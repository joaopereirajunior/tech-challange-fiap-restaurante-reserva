package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.ObterReservaPorIdUseCase;

class AtualizarReservaUseCaseImplTest {

	private AtualizarReservaUseCaseImpl atualizarReservaUseCaseImpl;
	private ObterReservaPorIdUseCase buscarReservaPorIdUseCase;
	
	@Mock
	ReservaGateway reservaGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		atualizarReservaUseCaseImpl = new AtualizarReservaUseCaseImpl(reservaGateway, buscarReservaPorIdUseCase);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirAtualizacaoDeRestaurante() {
		// Arrange
		Reserva reserva = gerarReserva();
		Reserva reservaModificada = gerarReserva();
		reservaModificada.setTotalPessoas(20L);
		reservaModificada.setConfirmada(true);
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));
		when(reservaGateway.atualizar(any(Reserva.class), any(Reserva.class)))
			.thenReturn(reservaModificada);
		
		// Act
		Reserva reservaObtida = atualizarReservaUseCaseImpl.execute(reserva.getId(),
				reservaModificada);
		
		// Assert
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, times(1)).atualizar(any(Reserva.class), any(Reserva.class));
		assertThat(reservaObtida).isInstanceOf(Restaurante.class).isNotNull();
		assertThat(reserva.getId()).isEqualTo(reservaObtida.getId());
		assertThat(reserva.getData()).isEqualTo(reservaObtida.getData());
		assertThat(reserva.getTotalPessoas()).isNotEqualTo(reservaObtida.getTotalPessoas());
		assertThat(reserva.getCliente().getId()).isNotEqualTo(reservaObtida.getCliente().getId());
		assertThat(reserva.getRestaurante().getId()).isEqualTo(reservaObtida.getRestaurante().getId());		
	}
	
	@Test
	void deveGerarExceptionAoAtualizarReserva_Reserva_Nao_Existe() {
		// Arrange
		Long idInexistente = 113123L;
		Reserva restaurante = gerarReserva();
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThatThrownBy(() -> atualizarReservaUseCaseImpl.execute(idInexistente,
				restaurante))
			.isInstanceOf(ReservaNaoEncontradaException.class)
			.hasMessage("O restaurante informado não existe.");
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, never()).atualizar(any(Reserva.class), any(Reserva.class));
	}
	
	private Reserva gerarReserva() {
		var cliente = new Cliente(1l, "Juca das Rosas", "07406565940");
		var restaurante = gerarRestaurante();
		return new Reserva(cliente, restaurante, 1L, 10L, LocalDateTime.now(), false);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
}
