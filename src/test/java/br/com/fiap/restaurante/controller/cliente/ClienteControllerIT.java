package br.com.fiap.restaurante.controller.cliente;

import br.com.fiap.restaurante.domain.Cliente;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());

    }

    @Nested
    class CriarCliente {
        @Test
        void deveCriarUmCliente() {
            Cliente cliente = new Cliente(1L, "João Silva", "07406565940");
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(cliente)
                    .when()
                    .post("/clientes")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .and()
                    .body("id", equalTo(1))
                    .body("nome", equalTo("João Silva"))
                    .body("cpf", equalTo("07406565940"));
        }
    }


}