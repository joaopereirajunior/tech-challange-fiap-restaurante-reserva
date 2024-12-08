package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.reservaimpl.ReservaGatewayImpl;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@Transactional
class ListarReservaUseCaseImplIT {

	@Autowired
	private ListarReservasUseCaseImpl listarReservasUseCaseImpl;

	@Autowired
	private ReservaGatewayImpl reservaGatewayImpl;

	@Autowired
	private ClienteGateway clienteGateway;

	@Autowired
	private RestauranteGateway restauranteGateway;

	@BeforeEach
	void setup() {
		registrarRestaurante();
		registrarCliente();
	}
	
	@Test
	void devePermitirListarReservas() {
		gerarReservas();

		List<Reserva> reservasObtidas = listarReservasUseCaseImpl.execute();
		
		assertThat(reservasObtidas).hasSize(3);
		assertThat(reservasObtidas).allSatisfy( reserva -> {
			assertThat(reserva).isNotNull().isInstanceOf(Reserva.class);
		});
	}
	
	private List<Reserva> gerarReservas() {
		List<Reserva> listaReservas = Arrays.asList(
			registrarReserva(1L),
			registrarReserva(2L),
			registrarReserva(3L)
		);
		return listaReservas;
	}

	private Reserva gerarReserva(Long id) {

		var cliente = new Cliente(1l, "João Silva", "07406565940");
		
		var restaurante = gerarRestaurante();

		return new Reserva(cliente, restaurante, id, 10L, LocalDateTime.now(), false, false, 0L, null);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(1L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 150);
	}


	private Cliente registrarCliente(){
		var cliente = new Cliente(1l, "João Silva", "07406565940");
		clienteGateway.salvar(cliente);
		return cliente;
	}

	private Restaurante registrarRestaurante() {
		var restaurante = gerarRestaurante();
		restauranteGateway.salvar(restaurante);

		return restaurante;
	}

	private Reserva registrarReserva(Long id) {		
		var reserva = gerarReserva(id);
		var retorno = reservaGatewayImpl.salvar(reserva);
		return retorno;
	}
}