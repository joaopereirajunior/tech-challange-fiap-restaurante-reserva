package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

class FinalizarReservaUseCaseImplTest {
	
	private FinalizarReservaUseCaseImpl finalizarReservaUseCaseImpl;
	@Mock
	private ReservaGateway reservaGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		finalizarReservaUseCaseImpl = new FinalizarReservaUseCaseImpl(reservaGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirFinalizarReserva() {
		// Arrange
		Reserva reserva = gerarReserva();
		Reserva reservaFinalizada = gerarReserva();

		reserva.setData(reservaFinalizada.data);

		reservaFinalizada.setFinalizada(true);
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));
		when(reservaGateway.finalizar(anyLong(), any(Reserva.class))).thenReturn(reservaFinalizada);
		
		// Act				
		var reservaOptional = reservaGateway.buscarPorId(anyLong());
		
		reserva = reservaOptional.get();
		
		var reservaObtida = finalizarReservaUseCaseImpl.execute(reserva.getId(), reservaFinalizada);

		// Assert
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, times(1)).finalizar(anyLong(), any(Reserva.class));
		assertThat(reservaObtida).isInstanceOf(Reserva.class).isNotNull();
		assertThat(reserva.getId()).isEqualTo(reservaObtida.getId());
		assertThat(reserva.getData()).isEqualTo(reservaObtida.getData());
		assertThat(reserva.getFinalizada()).isNotEqualTo(reservaObtida.getFinalizada());
		assertThat(reserva.getCliente().getId()).isEqualTo(reservaObtida.getCliente().getId());
		assertThat(reserva.getRestaurante().getId()).isEqualTo(reservaObtida.getRestaurante().getId());
	}
	
	@Test
	void deveGerarExceptionAoFinalizarReserva_Reserva_Nao_Existe() {
		// Arrange
		Long idInexistente = 113123L;
				
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.of(gerarReserva()));
		when(reservaGateway.finalizar(anyLong(), any(Reserva.class))).thenThrow(new ReservaNaoEncontradaException(idInexistente));

		Optional<Reserva> reserva = reservaGateway.buscarPorId(anyLong());

		// Act & Assert
		assertThatThrownBy(() -> finalizarReservaUseCaseImpl.execute(idInexistente, reserva.get()))
			.isInstanceOf(ReservaNaoEncontradaException.class)
			.hasMessage("Reserva não encontrada com o ID: " + idInexistente);
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, times(1)).finalizar(anyLong(), any(Reserva.class));
	}
	
	private Reserva gerarReserva() {
		var cliente = new Cliente(1l, "Juca das Rosas", "07406565940");
		var restaurante = gerarRestaurante();
		return new Reserva(cliente, restaurante, 1L, 10, LocalDateTime.now(), false, false, 0, null);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
}
