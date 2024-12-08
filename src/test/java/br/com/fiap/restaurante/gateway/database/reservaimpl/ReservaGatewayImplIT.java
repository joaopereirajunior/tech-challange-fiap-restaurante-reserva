package br.com.fiap.restaurante.gateway.database.reservaimpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.repository.reserva.ReservaRepository;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@Transactional
class ReservaGatewayImplIT {
	
	@Autowired
	private ReservaGatewayImpl reservaGatewayImpl;

	@Autowired
	private ClienteGateway clienteGateway;

	@Autowired
	private RestauranteGateway restauranteGateway;

	@Autowired
	private ReservaRepository reservaRepository;

	@BeforeEach
	void setup(){
		registrarRestaurante();
		registrarCliente();
	}
	
	@Test
	void devePermitirCriarTabela() {
	  long totalTabelasCriada = reservaRepository.count();
	  assertThat(totalTabelasCriada).isNotNegative();
	}
  
	@Test
	void devePermitirCriarReserva() {
		
		Reserva reservaGerada =  gerarReserva(1L);

		Reserva retorno = registrarReserva(1L);
		
		assertThat(retorno).isNotNull().isInstanceOf(Reserva.class);
		assertThat(retorno.getId()).isNotNull();
		//assertThat(retorno.getRestaurante()).isSameAs(reservaGerada.getRestaurante());
		//assertThat(retorno.getCliente()).isSameAs(reservaGerada.getCliente());
		assertThat(retorno.getTotalPessoas()).isNotNull();

		assertThat(retorno.getData()).isNotNull();
		assertThat(retorno.getConfirmada()).isEqualTo(reservaGerada.getConfirmada());
		assertThat(retorno.getFinalizada()).isEqualTo(reservaGerada.getFinalizada());
		assertThat(retorno.getNotaAvaliacao()).isEqualTo(reservaGerada.getNotaAvaliacao());
		assertThat(retorno.getComentarioAvaliacao()).isEqualTo(reservaGerada.getComentarioAvaliacao());
	}
	
	@Test
	void devePermitirBuscarReservaPorId() {
        Long id = 2L;
		var reserva = registrarReserva(id);
        Optional<Reserva> reservaOptional = reservaGatewayImpl.buscarPorId(id);

		assertThat(reservaOptional).isPresent();
		
		reservaOptional.ifPresent(reservaArmazenada ->{
			assertThat(reservaArmazenada.getId()).isEqualTo(reserva.getId());
			assertThat(reservaArmazenada.getFinalizada()).isEqualTo(reserva.getFinalizada());
			assertThat(reservaArmazenada.getConfirmada()).isEqualTo(reserva.getConfirmada());
			assertThat(reservaArmazenada.getData()).isEqualTo(reserva.getData());
			assertThat(reservaArmazenada.getFinalizada()).isEqualTo(reserva.getFinalizada());
			assertThat(reservaArmazenada.getNotaAvaliacao()).isEqualTo(reserva.getNotaAvaliacao());
			assertThat(reservaArmazenada.getComentarioAvaliacao()).isEqualTo(reserva.getComentarioAvaliacao());
		});
	}
	
	@Test
	void devePermitirListarReservas() {
		registrarReserva(3L);
		registrarReserva(4L);
		registrarReserva(5L);
		
        List<Reserva> retorno = reservaGatewayImpl.listarTodas();

        assertThat(retorno).hasSizeGreaterThan(0).allSatisfy(
        		reserva -> {
        			assertThat(reserva).isNotNull().isInstanceOf(Reserva.class);
        		}
        );
	}
	
	@Test
	void devePermitirListarReservas_retornaListaVaziaQuandoNenhumEncontrado() {
        List<Reserva> retorno = reservaGatewayImpl.listarTodas();
        assertThat(retorno).isEmpty();
	}
	
	@Test
	void devePermitirRemoverUmaReserva() {
        Long id = 6L;
		registrarReserva(id);
        reservaGatewayImpl.deletar(id);
		Optional<Reserva> reservaExcluida = reservaGatewayImpl.buscarPorId(id);
		assertThat(reservaExcluida).isEmpty();
	}
	
	@Test
	void devePermitirAtualizarUmaReserva() {
        
		Reserva reserva = registrarReserva(7L);

		Optional<Reserva> reservaOptional = reservaGatewayImpl.buscarPorId(reserva.getId());

		var reservaModificada = reservaOptional.get();
		reservaModificada.setCliente(reserva.getCliente());
		reservaModificada.setRestaurante(reserva.getRestaurante());
		reservaModificada.setTotalPessoas(20L);
		reservaModificada.setConfirmada(true);
		
		Reserva retorno = reservaGatewayImpl.atualizar(reserva.getId(), reservaModificada);
		
        assertThat(retorno).isNotNull().isInstanceOf(Reserva.class);
		assertThat(retorno.getId()).isEqualTo(reservaModificada.getId());
		assertThat(retorno.getConfirmada()).isEqualTo(reservaModificada.getConfirmada());		
		assertThat(retorno.getTotalPessoas()).isEqualTo(reservaModificada.getTotalPessoas());        
	}

	@Test
	void devePermitirFinalizarReserva() {
		Reserva reserva = registrarReserva(8L);
		Optional<Reserva> reservaOptional = reservaGatewayImpl.buscarPorId(reserva.getId());

		var reservaModificada = reservaOptional.get();
		reservaModificada.setCliente(reserva.getCliente());
		reservaModificada.setRestaurante(reserva.getRestaurante());
		reservaModificada.setFinalizada(true);
		reservaModificada.setRestaurante(reserva.getRestaurante());
		reservaModificada.setNotaAvaliacao(-10L);
		reservaModificada.setNotaAvaliacao(10L);
		reservaModificada.setNotaAvaliacao(5L);

        Reserva retorno = reservaGatewayImpl.finalizar(reserva.getId(), reservaModificada);
		
        assertThat(retorno).isNotNull().isInstanceOf(Reserva.class);
		assertThat(retorno.getId()).isEqualTo(reservaModificada.getId());
		assertThat(retorno.getFinalizada()).isEqualTo(reservaModificada.getFinalizada());		
		assertThat(retorno.getNotaAvaliacao()).isEqualTo(reservaModificada.getNotaAvaliacao()); 
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