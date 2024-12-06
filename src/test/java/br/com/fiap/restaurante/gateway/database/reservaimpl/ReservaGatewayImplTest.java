package br.com.fiap.restaurante.gateway.database.reservaimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class ReservaGatewayImplTest {
	
	@Mock
	ReservaGateway reservaGateway;
	@Mock
	private ClienteGateway clienteGateway;
	@Mock
	private RestauranteGateway restauranteGateway;
	
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
	void devePermitirCriarReserva() {
		// Arrange
		Reserva reserva = gerarReserva(1L);
		when(reservaGateway.salvar(any(Reserva.class))).thenReturn(reserva);
		
		// Act
		Reserva retorno = reservaGateway.salvar(reserva);
		
		// Assert
		verify(reservaGateway, times(1)).salvar(any(Reserva.class));
		assertThat(retorno).isInstanceOf(Reserva.class).isNotNull();
	}
	
	@Test
	void devePermitirBuscarReservaPorId() {
        // Arrange
        Long id = 1L;

		var reserva = gerarReserva(id);

        when(reservaGateway.buscarPorId(id)).thenReturn(Optional.of(reserva));

        // Act
        Optional<Reserva> resultado = reservaGateway.buscarPorId(id);

        // Assert
        verify(reservaGateway, times(1)).buscarPorId(anyLong());
        assertThat(resultado).isPresent();
        assertThat(reserva.id).isEqualTo(resultado.get().getId());
        assertThat(reserva.totalPessoas).isEqualTo(resultado.get().getTotalPessoas());
	}
	
	@Test
	void devePermitirListarReservas() {
        // Arrange
        List<Reserva> reservas = Arrays.asList(
                gerarReserva(1L),
                gerarReserva(2L),
				gerarReserva(3L)
            );
			
        when(reservaGateway.listarTodas()).thenReturn(reservas);

        // Act
        List<Reserva> retorno = reservaGateway.listarTodas();

        // Assert
        verify(reservaGateway, times(1)).listarTodas();
        assertThat(retorno).hasSize(3).allSatisfy(
        		reserva -> {
        			assertThat(reserva).isNotNull().isInstanceOf(Reserva.class);
        		}
        );
        assertThat(reservas.get(1).getId()).isEqualTo(2L);
        assertThat(reservas.get(0).getCliente().getId()).isEqualTo(1L);
        assertThat(reservas.get(1).getTotalPessoas()).isEqualTo(10);
	}
	
	@Test
	void devePermitirListarReservas_retornaListaVaziaQuandoNenhumEncontrado() {
		// Arrange
        when(reservaGateway.listarTodas()).thenReturn(Collections.emptyList());

        // Act
        List<Reserva> retorno = reservaGateway.listarTodas();

        // Assert
        verify(reservaGateway, times(1)).listarTodas();
        assertThat(retorno).isEmpty();
	}
	
	@Test
	void devePermitirRemoverUmaReserva() {
        // Arrange
        Long id = 1L;
        doNothing().when(reservaGateway).deletar(anyLong());
        
        // Act
        reservaGateway.deletar(id);

        // Assert
        verify(reservaGateway, times(1)).deletar(anyLong());
	}
	
	@Test
	void devePermitirAtualizarUmaReserva() {
        // Arrange
		Reserva reserva = gerarReserva(1L);
		Reserva reservaModificada = gerarReserva(1L);
		reservaModificada.setTotalPessoas(20L);
		reservaModificada.setConfirmada(true);
		when(reservaGateway.atualizar(any(Reserva.class), any(Reserva.class))).thenReturn(reservaModificada);
        
        // Act
        Reserva retorno = reservaGateway.atualizar(reserva, reservaModificada);
		
        // Assert
        verify(reservaGateway, times(1)).atualizar(any(Reserva.class), any(Reserva.class));
        assertThat(retorno).isNotNull();
		assertThat(retorno.getId()).isEqualTo(reservaModificada.getId());
		assertThat(retorno.getConfirmada()).isEqualTo(reservaModificada.getConfirmada());		
		assertThat(retorno.getTotalPessoas()).isEqualTo(reservaModificada.getTotalPessoas());        
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
