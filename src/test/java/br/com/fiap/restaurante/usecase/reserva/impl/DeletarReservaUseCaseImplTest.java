package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.repository.reserva.ReservaRepository;
import br.com.fiap.restaurante.gateway.database.reservaimpl.ReservaGatewayImpl;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class DeletarReservaUseCaseImplTest {

	private DeletarReservaUseCaseImpl deletarReservaUseCaseImpl;

	ReservaGateway reservaGateway;
	
	@Mock
	private ReservaRepository reservaRepository;
	@Mock
	private ClienteGateway clienteGateway;
	@Mock
	private RestauranteGateway restauranteGateway;

	AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
		reservaGateway = new ReservaGatewayImpl(reservaRepository, clienteGateway, restauranteGateway);
		deletarReservaUseCaseImpl = new DeletarReservaUseCaseImpl(reservaGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}

	@Test
	void devePermitirDeletarUmaReserva() {
		// Arrange
		Reserva reserva = gerarReserva();
		Long id = reserva.getId();
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.of(reserva));
		doNothing().when(reservaGateway).deletar(id);
		
		// Act
		deletarReservaUseCaseImpl.execute(id);
		
		// Assert
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, times(1)).deletar(anyLong());
	}
	
	@Test
	void deveGerarExceptionAoDeletarUmaReserva_Reserva_Nao_Existe() {
		Long idInexistente = 414129L;
		when(reservaGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThatThrownBy(() -> deletarReservaUseCaseImpl.execute(idInexistente))
			.isInstanceOf(RestauranteNaoEncontradoException.class)
			.hasMessage("A reserva informada não existe.");
		verify(reservaGateway, times(1)).buscarPorId(anyLong());
		verify(reservaGateway, never()).deletar(anyLong());
	}
	
	private Reserva gerarReserva() {
		var cliente = new Cliente(1L, "Juca das Rosas", "07406565940");
		var restaurante = gerarRestaurante();
		return new Reserva(cliente, restaurante, 1L, 10L, LocalDateTime.now(), false);
	}

	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}