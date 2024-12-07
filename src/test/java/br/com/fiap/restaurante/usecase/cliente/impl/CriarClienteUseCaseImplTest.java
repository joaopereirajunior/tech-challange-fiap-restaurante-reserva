package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.usecase.cliente.CriarClienteUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CriarClienteUseCaseImplTest {

    CriarClienteUseCase criarClienteUseCase;

    @Mock
    ClienteGateway clienteGateway;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        criarClienteUseCase = new CriarClienteUseCaseImpl(clienteGateway);
    }

    @AfterEach
    void teardown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarUmCliente() {
        //ARRANGE
        Cliente cliente = new Cliente(1L,"José","12345678901");
        when(clienteGateway.salvar(any(Cliente.class))).thenReturn(cliente);

        //ACT
        Cliente clienteObtido = criarClienteUseCase.execute(cliente);

        //ASSERT
        verify(clienteGateway, times(1)).salvar(any(Cliente.class));
        assertThat(clienteObtido).isNotNull();
        assertThat(cliente.getId()).isEqualTo(clienteObtido.getId());
        assertThat(cliente.getNome()).isEqualTo(clienteObtido.getNome());
        assertThat(cliente.getCpf()).isEqualTo(clienteObtido.getCpf());
    }
}
