package br.com.fiap.restaurante.gateway.database.restauranteimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.database.entity.restaurante.RestauranteEntity;
import br.com.fiap.restaurante.gateway.database.repository.restaurante.RestauranteRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class RestauranteGatewayImplTestIT {

	@Autowired
	private RestauranteGateway restauranteGateway;

	@Autowired
	private RestauranteRepository restauranteRepository;
	


	@BeforeEach
	void setup(){
		registrarRestaurantes();
	}

	@Test
	void devePermitirCriarTabela() {
		long totalTabelasCriada = restauranteRepository.count();
		assertThat(totalTabelasCriada).isNotNegative();
	}
	
	@Test
	void devePermitirRegistrarRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();

		// Act
		Restaurante retorno = restauranteGateway.salvar(restaurante);
		
		// Assert

		assertThat(retorno).isInstanceOf(Restaurante.class).isNotNull();
	}
	
	@Test
	void devePermitirBuscarRestaurantePorId() {
        // Arrange
		Restaurante restaurante = registrarRestaurante();

        // Act
        Optional<Restaurante> resultado = restauranteGateway.buscarPorId(restaurante.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat("Heroe's Burguer").isEqualTo(resultado.get().getNome());
		assertThat("Rua de Teste, 59").isEqualTo(resultado.get().getLocalizacao());
		assertThat("Hamburguers e Lanches").isEqualTo(resultado.get().getTipoCozinha());
		assertThat("Das 9h às 18h - Seg a Sex.").isEqualTo(resultado.get().getHorarioFuncionamento());
		assertThat(15).isEqualTo(resultado.get().getCapacidade());

	}
	
	@Test
	void devePermitirListarRestaurantes() {
		// Act
		List<Restaurante> restaurantesObtidos = restauranteGateway.listarTodos();


		//Assert
		assertThat(restaurantesObtidos)
				.isNotEmpty()
				.hasSizeGreaterThan(0);
		assertThat(restaurantesObtidos).allSatisfy(restaurante -> {
			assertThat(restaurante).isNotNull();
		});

	}

	@Test
	void devePermitirRemoverUmRestaurante() {
		//Arrange
		var restauranteRegistrado = registrarRestaurante();
		//Act
		restauranteGateway.deletar(restauranteRegistrado.getId());
		//Assert
		var restauranteDeletado = restauranteGateway.buscarPorId(restauranteRegistrado.getId());
		assertThat(restauranteDeletado).isEmpty();
	}
	
	@Test
	void devePermitirAtualizarUmRestaurante() {
        // Arrange
		Restaurante restaurante = registrarRestaurante();
		Restaurante restauranteModificado = gerarRestaurante();
		restauranteModificado.setLocalizacao("Av. Salvador, 101");
		restauranteModificado.setTipoCozinha("Lanches Diversos");
		restauranteModificado.setCapacidade(30);

        // Act
        Restaurante retorno = restauranteGateway.atualizar(restaurante, restauranteModificado);

        // Assert
        assertThat(retorno).isNotNull();
		assertThat(retorno.getNome()).isEqualTo(restauranteModificado.getNome());
		assertThat(retorno.getLocalizacao()).isEqualTo(restauranteModificado.getLocalizacao());
		assertThat(retorno.getTipoCozinha()).isEqualTo(restauranteModificado.getTipoCozinha());
		assertThat(retorno.getHorarioFuncionamento()).isEqualTo(restauranteModificado.getHorarioFuncionamento());
		assertThat(retorno.getCapacidade()).isEqualTo(restauranteModificado.getCapacidade());
        
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(null, "Heroe's Burguer",
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
	private RestauranteEntity gerarRestauranteEntity() {
		return new RestauranteEntity(null, "Heroe's Burguer",
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}

	private Restaurante registrarRestaurante() {
		var restaurante = gerarRestaurante();
		var restauranteObtido = restauranteGateway.salvar(restaurante);

		return restauranteObtido;

	}

	private List<Restaurante> gerarRestaurantes() {
		List<Restaurante> listaRestaurantes = Arrays.asList(
				new Restaurante(null, "Pizza Master",
						"Avenida Paulista, 1000", "Pizzas e Massas", "Das 11h às 23h - Todos os dias", 50),

				new Restaurante(null, "Sushi House",
						"Rua dos Três Irmãos, 45", "Sushis e Comida Japonesa", "Das 12h às 22h - Seg a Sáb.", 30),

				new Restaurante(null, "Taco Loco",
						"Rua das Flores, 120", "Comida Mexicana", "Das 10h às 20h - Todos os dias", 40)
		);
		return listaRestaurantes;
	}



	private void registrarRestaurantes() {
		List<Restaurante> restaurantes = gerarRestaurantes();
		for (Restaurante restaurante : restaurantes) {
			restauranteGateway.salvar(restaurante);
		}

	}

}
