package br.com.fiap.restaurante.controller.reserva;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;

import java.time.LocalDateTime;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.reservaimpl.ReservaGatewayImpl;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import br.com.fiap.restaurante.utils.RestauranteHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservaControllerIT {

  @LocalServerPort
  private int port;

  @Autowired
	private ReservaGatewayImpl reservaGatewayImpl;

	@Autowired
	private ClienteGateway clienteGateway;

	@Autowired
	private RestauranteGateway restauranteGateway;

  @BeforeEach
  public void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    registrarRestaurante();
		registrarCliente();
  }

  private Cliente registrarCliente(){
		var cliente = new Cliente(1l, "João Silva", "07406565940");
		clienteGateway.salvar(cliente);
		return cliente;
	}

	private Restaurante registrarRestaurante() {
		var restaurante = gerarRestaurante();
		restauranteGateway.salvar(restaurante);

		return restaurante;
	}

	private Reserva registrarReserva() {		
		var reserva = gerarReserva();
		var retorno = reservaGatewayImpl.salvar(reserva);
		return retorno;
	}

  private Reserva gerarReserva() {

		var cliente = new Cliente(1l, "João Silva", "07406565940");
		
		var restaurante = gerarRestaurante();

		return new Reserva(cliente, restaurante, 0L, 10, LocalDateTime.now(), false, false, 0, null);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(1L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 150);
	}

  @Nested
  class RegistrarReserva {

    @Test
    void devePermitirCriarReserva() throws Exception {
      var reservaRequest = RestauranteHelper.gerarReservaRequest();

      given()
        .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(reservaRequest)
          .when()
          .post("/reservas")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"))
          .body("$", hasKey("id"))
          .body("$", hasKey("confirmada"))
          .body("$", hasKey("totalPessoas"))
          .body("$", hasKey("data"))
          .body("totalPessoas", equalTo(reservaRequest.getTotalPessoas()))
          .body("id", greaterThan(0));
    }
   }

  @Nested
  class BuscarReserva {

    @Test
    void devePermitirBuscarReserva() {
      var reserva = registrarReserva();
      given()
      .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/reservas/{id}", reserva.getId())
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }
  }

  @Nested
  class AlterarReserva {

    @Test
    void devePermirirAlterarReserva() {      
      var reserva = registrarReserva();

      reserva.setTotalPessoas(1);

      given()
      .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(reserva)
          .when()
          .put("/reservas/{id}", reserva.id)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarReserva_PayloadComXml() {
      var id = 1;
      String xmlPayload = "<mensagem><usuario>John</usuario><conteudo>Conteúdo da mensagem</conteudo></mensagem>";

      given()
      .filter(new AllureRestAssured())
          .contentType(ContentType.XML)
          .body(xmlPayload)
          .when()
          .put("/reservas/{id}", id)
          .then()
          .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }
  }

  @Nested
  class DeletarReserva {

    @Test
    void deveDeletarReserva() {
      var reserva = registrarReserva();
      given()
      .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/reservas/{id}", reserva.getId())
          .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
    }
  }

  @Nested
  class ListarReserva {

    @Test
    void deveListarReservas() {
      registrarReserva();
      registrarReserva();
      registrarReserva();
      given()
      .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/reservas")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("./schemas/ReservasSchema.json"));
    }
  }

  @Nested
  class FinalizarReserva {

    @Test
    void devePermirirFinalizarReserva() {      
      var reserva = registrarReserva();

      reserva.setFinalizada(true);
      reserva.setNotaAvaliacao(5);

      given()
      .filter(new AllureRestAssured())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(reserva)
          .when()
          .patch("/reservas/{id}", reserva.id)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }
  }
}
