package br.com.fiap.restaurante.usecase.cliente.impl;


import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class DeletarClienteUseCaseImplIT {

    @Autowired
    private ClienteGateway clienteGateway;

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
    void deveDeletarCliente(){
        //Arrange
        var cliente = gerarCliente();
        clienteGateway.salvar(cliente);
        //Act
        clienteGateway.deletar(cliente.getId());
        //Assert
        var clienteDeletado = clienteGateway.buscarPorId(cliente.getId());
        assertThat(clienteDeletado).isEmpty();
    }

    private Cliente gerarCliente(){
        return Cliente.builder().id(1L).nome("José").cpf("12345678901").build();
    }
}
