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
import br.com.fiap.restaurante.gateway.database.entity.reserva.ReservaEntity;
import br.com.fiap.restaurante.gateway.database.repository.reserva.ReservaRepository;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class ReservaGatewayImplTest {
	
	@Mock
	ReservaGateway reservaGateway;	
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
		//reservaGateway = new ReservaGatewayImpl(reservaRepository, clienteGateway, restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirCriarReserva() {
		// Arrange
		Reserva reserva = gerarReserva();
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
        when(reservaRepository.findById(id)).thenReturn(Optional.of(gerarReservaEntity()));

        // Act
        Optional<Reserva> resultado = reservaGateway.buscarPorId(id);

        // Assert
        verify(reservaRepository, times(1)).findById(anyLong());
        assertThat(resultado).isPresent();
        assertThat(id).isEqualTo(resultado.get().getId());
        assertThat(10L).isEqualTo(resultado.get().getTotalPessoas());
	}
	
	@Test
	void devePermitirListarReservas() {
        // Arrange
        List<ReservaEntity> reservas = Arrays.asList(
                new ReservaEntity(1L, 1L, 5L, 10L, LocalDateTime.now(), false),
                new ReservaEntity(2L, 1L, 5L, 10L, LocalDateTime.now(), false)
            );
			
        when(reservaRepository.findAll()).thenReturn(reservas);

        // Act
        List<Reserva> retorno = reservaGateway.listarTodas();

        // Assert
        verify(reservaRepository, times(1)).findAll();
        assertThat(retorno).hasSize(2).allSatisfy(
        		reserva -> {
        			assertThat(reserva).isNotNull().isInstanceOf(Reserva.class);
        		}
        );
        assertThat(reservas.get(1).getId()).isEqualTo(2L);
        assertThat(reservas.get(0).getIdClient()).isEqualTo(1L);
        assertThat(reservas.get(1).getTotalPessoas()).isEqualTo(10);
	}
	
	@Test
	void devePermitirListarReservas_retornaListaVaziaQuandoNenhumEncontrado() {
		// Arrange
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Reserva> retorno = reservaGateway.listarTodas();

        // Assert
        verify(reservaRepository, times(1)).findAll();
        assertThat(retorno).isEmpty();
	}
	
	@Test
	void devePermitirRemoverUmaReserva() {
        // Arrange
        Long id = 1L;
        doNothing().when(reservaRepository).deleteById(id);
        
        // Act
        reservaRepository.deleteById(id);

        // Assert
        verify(reservaRepository, times(1)).deleteById(anyLong());
	}
	
	@Test
	void devePermitirAtualizarUmaReserva() {
        // Arrange
		Reserva reserva = gerarReserva();
		Reserva reservaModificada = gerarReserva();
		reservaModificada.setTotalPessoas(20L);
		reservaModificada.setConfirmada(true);
		when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(gerarReservaEntity());
        
        // Act
        Reserva retorno = reservaGateway.atualizar(reserva, reservaModificada);
		
        // Assert
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
        assertThat(retorno).isNotNull();
		assertThat(retorno.getId()).isEqualTo(reservaModificada.getId());
		assertThat(retorno.getConfirmada()).isEqualTo(reservaModificada.getConfirmada());		
		assertThat(retorno.getTotalPessoas()).isEqualTo(reservaModificada.getTotalPessoas());        
	}
	
	private Reserva gerarReserva() {

		var cliente = new Cliente(1l, "Juca das Rosas", "07406565940");
		
		var restaurante = gerarRestaurante();

		//cliente = clienteGateway.salvar(cliente);
		
		//restaurante = restauranteGateway.salvar(restaurante);

		return new Reserva(cliente, restaurante, 1L, 10L, LocalDateTime.now(), false);
	}
	
	private ReservaEntity gerarReservaEntity() {
		return new ReservaEntity(1L, 1L, 5L, 10L, LocalDateTime.now(), false);
	}

	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}
