package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.repository.reserva.ReservaRepository;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class CriarReservaUseCaseImplTest {

	private CriarReservaUseCaseImpl criarReservaUseCaseImpl;
	
	@Mock
	private ReservaGateway reservaGateway;	
	@Mock
	private ReservaRepository reservaRepository;
	@Mock
	private ClienteGateway clienteGateway;
	@Mock
	private RestauranteGateway restauranteGateway;

	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		criarReservaUseCaseImpl = new CriarReservaUseCaseImpl(reservaGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirCriacaoDeReserva() {
		// Arrange
		Reserva reserva = gerarReserva();
		when(reservaGateway.salvar(any(Reserva.class))).thenReturn(reserva);
		
		// Act
		Reserva retorno = criarReservaUseCaseImpl.execute(reserva);
		
		// Assert
		verify(reservaGateway, times(1)).salvar(any(Reserva.class));

		assertThat(retorno).isInstanceOf(Reserva.class).isNotNull();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getId()).isEqualTo(reserva.getId());
		assertThat(retorno.getCliente().getId()).isEqualTo(reserva.getCliente().getId());
		assertThat(retorno.getRestaurante().getId()).isEqualTo(reserva.getRestaurante().getId());
		assertThat(retorno.getTotalPessoas()).isEqualTo(reserva.getTotalPessoas());
		assertThat(retorno.getConfirmada()).isEqualTo(reserva.getConfirmada());
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
