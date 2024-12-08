package br.com.fiap.restaurante.usecase.reserva.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@Transactional
class CriarReservaUseCaseImplIT {

	@Autowired
	private CriarReservaUseCaseImpl criarReservaUseCaseImpl;	

	@Autowired
	private ClienteGateway clienteGateway;

	@Autowired
	private RestauranteGateway restauranteGateway;

	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		registrarCliente();
		registrarRestaurante();
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirCriacaoDeReserva() {
		Reserva reserva = gerarReserva(1L);
		Reserva retorno = criarReservaUseCaseImpl.execute(reserva);
		
		assertThat(retorno).isInstanceOf(Reserva.class).isNotNull();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getId()).isEqualTo(reserva.getId());
		assertThat(retorno.getCliente().getId()).isEqualTo(reserva.getCliente().getId());
		assertThat(retorno.getRestaurante().getId()).isEqualTo(reserva.getRestaurante().getId());
		assertThat(retorno.getTotalPessoas()).isEqualTo(reserva.getTotalPessoas());
		assertThat(retorno.getConfirmada()).isEqualTo(reserva.getConfirmada());
	}

	@Test
	void deveGerarExceptionSeNaoHouverReserva_Disponibilidade_Para_Data_Selecionada() {
		
		Reserva reserva = gerarReserva(1L);
		reserva.setTotalPessoas(151L);
		
		assertThatThrownBy(() -> criarReservaUseCaseImpl.execute(reserva))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Reserva indisponível, para a data selecionada, escolha uma outra data.");
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
}
