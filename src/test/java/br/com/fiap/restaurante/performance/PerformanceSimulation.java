package br.com.fiap.restaurante.performance;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.time.Duration;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class PerformanceSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080").header("Content-Type",
            "application/json");

    ActionBuilder criarCliente = http("criar cliente")
            .post("/clientes")
            .body(StringBody("{ \"nome\": \"João Silva\", \"cpf\": \"123.456.789-00\" }"))
            .check(status().is(200));

    ActionBuilder criarRestaurante = http("criar restaurante")
            .post("/restaurantes")
            .body(StringBody(
                    "{\"nome\": \"Sabor da Arabia\",\"localizacao\": \"Rua XPTO, 21\",\"tipoCozinha\": \"Arabe\",\"horarioFuncionamento\": \"24 horas\",\"capacidade\": 44}"))
            .check(status().is(200));
    
    ActionBuilder criarReserva = http("criar reserva")
            .post("/reservas")
            .body(StringBody(
                    "{\"id\": 3,\"restaurante\": {\"id\": 1,\"nome\": \"Sabor da Arabia\",\"localizacao\": \"Rua XPTO, 21\",\"tipoCozinha\": \"Arabe\",\"horarioFuncionamento\": \"24 horas\",\"capacidade\": 44},\"cliente\": {\"id\": 1,\"nome\": \"João Silva\", \"cpf\":\"123.456.789-00\"},\"data\": \"2024-12-07T17:35:40\",\"confirmada\": 1,\"totalPessoas\": 4}"))
            .check(status().is(200));        

    ScenarioBuilder cenarioCriarCliente = scenario("criar cliente").exec(criarCliente);

    ScenarioBuilder cenarioCriarRestaurante = scenario("criar restaurante").exec(criarRestaurante);

    ScenarioBuilder cenarioCriarReserva = scenario("criar reserva").exec(criarReserva);

    {

        setUp(
                cenarioCriarCliente.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(8)), // Aquecimento da aplicacao
                        constantUsersPerSec(10) // usuarios constantes usando
                                .during(Duration.ofSeconds(8)),
                        rampUsersPerSec(10) // Desaceleracao dos testes
                                .to(1)
                                .during(Duration.ofSeconds(8))),
                cenarioCriarRestaurante.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(8)), // Aquecimento da aplicacao
                        constantUsersPerSec(10) // usuarios constantes usando
                                .during(Duration.ofSeconds(8)),
                        rampUsersPerSec(10) // Desaceleracao dos testes
                                .to(1)
                                .during(Duration.ofSeconds(8))),
                cenarioCriarReserva.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(8)), // Aquecimento da aplicacao
                        constantUsersPerSec(10) // usuarios constantes usando
                                .during(Duration.ofSeconds(8)),
                        rampUsersPerSec(10) // Desaceleracao dos testes
                                .to(1)
                                .during(Duration.ofSeconds(8)))

        ).protocols(httpProtocol)
        .assertions(global().responseTime().max().lt(5000)); // Tempo maximo inferior de 1500 milesegundos

    }

}
