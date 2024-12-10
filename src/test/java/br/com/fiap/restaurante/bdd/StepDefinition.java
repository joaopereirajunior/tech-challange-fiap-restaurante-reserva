package br.com.fiap.restaurante.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.utils.RestauranteHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class StepDefinition {
    private Response response;

    private Cliente clienteResponse;
    private Restaurante restauranteResponse;
    private Reserva reservaResponse;
    
    private String ENDPOINT_CLIENTES = "http://localhost:9090/clientes";
    private String ENDPOINT_RESTAURANTES = "http://localhost:9090/restaurantes";
    private String ENDPOINT_RESERVAS = "http://localhost:9090/reservas";
    
    @Quando("submeter um novo cliente")
    public Cliente submeterNovoCliente() {
        var clienteRequest = RestauranteHelper.gerarClienteRequest();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteRequest)
                .when().post(ENDPOINT_CLIENTES);

        return response.then().extract().as(Cliente.class);
    }

    @Então("o cliente é registrado com sucesso")
    public void clienteRegistradoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ClienteSchema.json"));
    }

    @Dado("que um cliente já foi registrado")
    public void clienteJaPublicado() {
        clienteResponse = submeterNovoCliente();
    }

    @Quando("requisitar a busca do cliente")
    public void requisitarBuscarCliente() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_CLIENTES + "/" + clienteResponse.getId().toString());
    }

    @Então("o cliente é exibido com sucesso")
    public void clienteExibidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ClienteSchema.json"));
    }

    @Quando("requisitar a lista de clientes")
    public void requisitarListaClientes() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_CLIENTES);
    }

    @Então("os clientes são exibidos com sucesso")
    public void clientesSaoExibidosComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Quando("requisitar a alteração do cliente")
    public void requisitarAlteracaoDoCliente() {
        clienteResponse.setNome("Joaquim Manoel");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteResponse)
                .when()
                .put(ENDPOINT_CLIENTES + "/" + clienteResponse.getId().toString());
    }

    @Então("o cliente é atualizado com sucesso")
    public void clienteAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Joaquim Manoel"))
                .body(matchesJsonSchemaInClasspath("./schemas/ClienteSchema.json"));
    }

    @Quando("requisitar a exclusão do cliente")
    public void requisitarExclusaoDoCliente() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT_CLIENTES + "/" + clienteResponse.getId().toString());
    }

    @Então("o cliente é removido com sucesso")
    public void clienteRemovidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Quando("submeter um novo restaurante")
    public Restaurante submeterNovoRestaurante() {
        var restauranteRequest = RestauranteHelper.gerarRestauranteRequest();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteRequest)
                .when().post(ENDPOINT_RESTAURANTES);

        return response.then().extract().as(Restaurante.class);
    }

    @Então("o restaurante é registrado com sucesso")
    public void restauranteRegistradoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/RestauranteSchema.json"));
    }

    @Dado("que um restaurante já foi registrado")
    public void restauranteJaPublicado() {
        restauranteResponse = submeterNovoRestaurante();
    }

    @Quando("requisitar a busca do restaurante")
    public void requisitarBuscarRestaurante() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_RESTAURANTES + "/" + restauranteResponse.getId().toString());
    }

    @Então("o restaurante é exibido com sucesso")
    public void restauranteExibidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/RestauranteSchema.json"));
    }

    @Quando("requisitar a lista de restaurantes")
    public void requisitarListaRestaurantes() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_RESTAURANTES);
    }

    @Então("os restaurantes são exibidos com sucesso")
    public void restaurantesSaoExibidosComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Quando("requisitar a alteração do restaurante")
    public void requisitarAlteracaoDoRestaurante() {
        restauranteResponse.setNome("Bom Sabor");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteResponse)
                .when()
                .put(ENDPOINT_RESTAURANTES + "/" + restauranteResponse.getId().toString());
    }

    @Então("o restaurante é atualizado com sucesso")
    public void restauranteAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Bom Sabor"))
                .body(matchesJsonSchemaInClasspath("./schemas/RestauranteSchema.json"));
    }

    @Quando("requisitar a exclusão do restaurante")
    public void requisitarExclusaoDoRestaurante() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT_RESTAURANTES + "/" + restauranteResponse.getId().toString());
    }

    @Então("o restaurante é removido com sucesso")
    public void restauranteRemovidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }



    @Quando("submeter uma nova reserva")
    public Reserva submeterNovaReserva() {
        var reservaRequest = RestauranteHelper.gerarReservaRequest();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaRequest)
                .when().post(ENDPOINT_RESERVAS);

        return response.then().extract().as(Reserva.class);
    }

    @Então("a reserva é registrada com sucesso")
    public void reservaRegistradaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }

    @Dado("que uma reserva já foi registrada")
    public void reservaJaPublicada() {
        reservaResponse = submeterNovaReserva();
    }

    @Quando("requisitar a busca da reserva")
    public void requisitarBuscarReserva() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_RESERVAS + "/" + reservaResponse.getId().toString());
    }

    @Então("a reserva é exibida com sucesso")
    public void reservaExibidaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }

    @Quando("requisitar a lista de reservas")
    public void requisitarListaReservas() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_RESERVAS);
    }

    @Então("as reservas são exibidas com sucesso")
    public void reservasSaoExibidasComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Quando("requisitar a alteração da reserva")
    public void requisitarAlteracaoDaReserva() {
        reservaResponse.setTotalPessoas(11);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaResponse)
                .when()
                .put(ENDPOINT_RESERVAS + "/" + reservaResponse.getId().toString());
    }

    @Então("a reserva é atualizada com sucesso")
    public void reservaAtualizadaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("totalPessoas", equalTo(11))
                .body(matchesJsonSchemaInClasspath("./schemas/ReservaSchema.json"));
    }

    @Quando("requisitar a exclusão da reserva")
    public void requisitarExclusaoDaReserva() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT_RESERVAS + "/" + reservaResponse.getId().toString());
    }

    @Então("a reserva é removida com sucesso")
    public void reservaRemovidaComSucesso() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }



}
