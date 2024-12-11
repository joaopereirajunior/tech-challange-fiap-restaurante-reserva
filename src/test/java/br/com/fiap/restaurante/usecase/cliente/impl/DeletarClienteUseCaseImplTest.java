package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.exception.ClienteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.MBeanServerConnection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DeletarClienteUseCaseImplTest {

    DeletarClienteUseCaseImpl deletarClienteUseCaseImpl;

    @Mock
    ClienteGateway clienteGateway;
    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        deletarClienteUseCaseImpl = new DeletarClienteUseCaseImpl(clienteGateway);
    }

    @AfterEach
    void teardown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirDeletarUmClientePeloId() {
        // Arrange
        Long id = 1L;
        Cliente cliente = new Cliente(id,"José","12345678901");
        Long idCliente = cliente.getId();
        when(clienteGateway.buscarPorId(idCliente)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteGateway).deletar(idCliente);



        // Act
        deletarClienteUseCaseImpl.execute(idCliente);

        // Assert
        verify(clienteGateway, times(1)).deletar(anyLong());
    }

    @Test
    void deveGerarExceptionAoDeletarUmCliente_Cliente_Nao_Existe() {
        // Arrange
        Long idInexistente = 12315L;
        when(clienteGateway.buscarPorId(anyLong())).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThatThrownBy(() -> deletarClienteUseCaseImpl.execute(idInexistente))
                .isInstanceOf(ClienteNaoEncontradoException.class)
                .hasMessage("Cliente não encontrado");
        verify(clienteGateway, times(1)).buscarPorId(anyLong());
        verify(clienteGateway, never()).deletar(anyLong());

    }


    @Test
    void deveGerarExceptionQuandoIdForNulo() {
        // Act & Assert
        assertThatThrownBy(() -> deletarClienteUseCaseImpl.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID cliente não pode ser nulo");
    }


}
