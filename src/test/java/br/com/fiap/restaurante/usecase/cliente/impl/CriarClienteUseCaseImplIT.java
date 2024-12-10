package br.com.fiap.restaurante.usecase.cliente.impl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class CriarClienteUseCaseImplIT {

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
    void devePermitirCriarTabela(){

        var totalRegistros = clienteGateway.listarTodos().stream().count();

        assertThat(totalRegistros).isNotNegative();

    }
    @Test
    void devePermitirCadastrarCliente(){
        //Arrange
        var cliente = gerarCliente();
        //Act
        var clienteSalvo = clienteGateway.salvar(cliente);
        //Assert
        assertThat(clienteSalvo)
                .isInstanceOf(Cliente.class)
                .isNotNull();
        assertThat(clienteSalvo.getId()).isEqualTo(cliente.getId());

    }

    private Cliente gerarCliente(){
        return Cliente.builder().id(1L).nome("José").cpf("12345678901").build();
    }

}
