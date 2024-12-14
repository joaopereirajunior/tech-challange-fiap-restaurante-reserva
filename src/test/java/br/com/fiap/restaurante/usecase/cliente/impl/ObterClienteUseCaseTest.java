package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ObterClienteUseCaseTest {

    ObterClienteUseCaseImpl obterClienteUseCase;

    @Mock
    ClienteGateway clienteGateway;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        obterClienteUseCase = new ObterClienteUseCaseImpl(clienteGateway);
    }

    @AfterEach
    void teardown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirObterTodosClientes() {
        // Arrange
        Cliente cliente = new Cliente(1L, "José", "12345678901");
        
        when(clienteGateway.listarTodos()).thenReturn(List.of(cliente));
        // Act
        List<Cliente> clientesObtidos = obterClienteUseCase.execute();
        // Assert
        verify(clienteGateway,times(1)).listarTodos();
        assertThat(clientesObtidos).isNotNull();
        assertThat(clientesObtidos).isNotEmpty();
        assertThat(clientesObtidos).hasSize(1);
        assertThat(clientesObtidos.get(0)).isInstanceOf(Cliente.class).isNotNull();
        assertThat(clientesObtidos.get(0).getId()).isEqualTo(cliente.getId());
        assertThat(clientesObtidos.get(0).getNome()).isEqualTo(cliente.getNome());
        assertThat(clientesObtidos.get(0).getCpf()).isEqualTo(cliente.getCpf());
    }



}
