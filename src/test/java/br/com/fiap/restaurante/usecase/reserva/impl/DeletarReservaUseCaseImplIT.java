package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.reservaimpl.ReservaGatewayImpl;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class DeletarReservaUseCaseImplIT {
	
	@Autowired
	private DeletarReservaUseCaseImpl deletarReservaUseCaseImpl;
	
	@Autowired
	ReservaGateway reservaGateway;

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
	void devePermitirDeletarUmaReserva() {

		var reserva = registrarReserva();		

		var reservaOptional = reservaGateway.buscarPorId(reserva.getId());

		assertThat(reservaOptional).isPresent();

		deletarReservaUseCaseImpl.execute(reserva.getId());
		
		reservaOptional = reservaGateway.buscarPorId(reserva.getId());
		
		assertThat(reservaOptional).isEmpty();
	}
	
	private Reserva gerarReserva() {

		var cliente = new Cliente(1l, "João Silva", "07406565940");
		
		var restaurante = gerarRestaurante();

		return new Reserva(cliente, restaurante, 0L, 10, LocalDateTime.now(), false, false, 0, null);
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

	private Reserva registrarReserva() {		
		var reserva = gerarReserva();
		var retorno = reservaGatewayImpl.salvar(reserva);
		return retorno;
	}

}