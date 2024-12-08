package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ObterClientePorIdUseCaseImplTest {

    ObterClientePorIdUseCaseImpl obterClientePorIdUseCaseImpl;
    @Mock
    ClienteGateway clienteGateway;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        obterClientePorIdUseCaseImpl = new ObterClientePorIdUseCaseImpl(clienteGateway);
    }

    @AfterEach
    void teardown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirObterUmClientePeloId() {
        // Arrange
        Cliente cliente = new Cliente(1L,"José","12345678901");
        Long id = cliente.getId();
        when(clienteGateway.buscarPorId(anyLong())).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> clienteObtido = obterClientePorIdUseCaseImpl.execute(id);

        // Assert
        verify(clienteGateway, times(1)).buscarPorId(anyLong());
        assertThat(clienteObtido).isPresent();
        assertThat(clienteObtido.get()).isInstanceOf(Cliente.class).isNotNull();
        assertThat(clienteObtido.get().getId()).isEqualTo(cliente.getId());
        assertThat(clienteObtido.get().getNome()).isEqualTo(cliente.getNome());
        assertThat(clienteObtido.get().getCpf()).isEqualTo(cliente.getCpf());
    }

}
