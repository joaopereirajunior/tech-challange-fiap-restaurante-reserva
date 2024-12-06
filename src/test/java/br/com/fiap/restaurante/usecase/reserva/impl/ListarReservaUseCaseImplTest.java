package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.usecase.reserva.ListarReservasUseCase;

class ListarReservaUseCaseImplTest {

	@Mock
	private ListarReservasUseCase listarReservasUseCase;

	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirListarReservas() {
		// Arrange
		List<Reserva> listaReservas = gerarReservas();
		when(listarReservasUseCase.execute()).thenReturn(listaReservas);
		
		// Act
		List<Reserva> reservasObtidas = listarReservasUseCase.execute();
		
		// Assert
		verify(listarReservasUseCase, times(1)).execute();
		assertThat(reservasObtidas).hasSize(3);
		assertThat(reservasObtidas).allSatisfy( reserva -> {
			assertThat(reserva).isNotNull().isInstanceOf(Reserva.class);
		});

	}
	
	private List<Reserva> gerarReservas() {
		List<Reserva> listaReservas = Arrays.asList(
			gerarReserva(1L),
			gerarReserva(2L),
			gerarReserva(3L)
		);
		return listaReservas;
	}

	private Reserva gerarReserva(Long id) {
		var cliente = new Cliente(1l, "Juca das Rosas", "07406565940");
		var restaurante = gerarRestaurante();
		return new Reserva(cliente, restaurante, id, 10L, LocalDateTime.now(), false);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}
