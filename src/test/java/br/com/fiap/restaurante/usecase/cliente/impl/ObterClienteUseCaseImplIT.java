package br.com.fiap.restaurante.usecase.cliente.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase
public class ObterClienteUseCaseImplIT {


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
    void devePermitirListarClientes(){

        //Arrange
        var listaClientes = Arrays.asList(gerarCliente(), gerarCliente());

        clienteGateway.salvar(listaClientes.get(0));

        //Act
        var retornoClientes = clienteGateway.listarTodos();

        //Assert
        assertThat(retornoClientes)
                .isNotEmpty()
                .hasSizeGreaterThan(0);

    }

    private Cliente gerarCliente(){
        return Cliente.builder().id(1L).nome("José").cpf("12345678901").build();
    }

}
