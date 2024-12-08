package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.reservaimpl.ReservaGatewayImpl;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@Transactional
class FinalizarReservaUseCaseImplIT {
	
	@Autowired
	private FinalizarReservaUseCaseImpl finalizarReservaUseCaseImpl;
		
	@Autowired
	private ReservaGateway reservaGateway;

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
	void devePermitirFinalizarReserva() {		
		Reserva reserva = registrarReserva(1L);
		Reserva reservaFinalizada = gerarReserva(1L);

		reserva.setData(reservaFinalizada.data);

		reservaFinalizada.setFinalizada(true);
		
		var reservaOptional = reservaGateway.buscarPorId(reserva.getId());
		
		reserva = reservaOptional.get();
		
		var reservaObtida = finalizarReservaUseCaseImpl.execute(reserva.getId(), reservaFinalizada);

		assertThat(reservaOptional).isPresent();
		assertThat(reservaObtida).isInstanceOf(Reserva.class).isNotNull();
		assertThat(reserva.getId()).isEqualTo(reservaObtida.getId());
		assertThat(reserva.getFinalizada()).isNotEqualTo(reservaObtida.getFinalizada());
	}
	
	@Test
	void deveGerarExceptionAoFinalizarReserva_Reserva_Nao_Existe() {
		Long idInexistente = 113123L;
		
		var reserva = gerarReserva(1L);
		assertThatThrownBy(() -> finalizarReservaUseCaseImpl.execute(idInexistente, reserva))
			.isInstanceOf(ReservaNaoEncontradaException.class)
			.hasMessage("Reserva não encontrada com o ID: " + idInexistente);
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
