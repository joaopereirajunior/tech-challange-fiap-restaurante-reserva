package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ListarRestaurantesUseCaseImplIT {

	@Autowired
	private ListarRestaurantesUseCaseImpl listarRestaurantesUseCaseImpl;

	@Autowired
	RestauranteGateway restauranteGateway;

	@BeforeEach
	void setup(){
		registrarRestaurantes();
	}

	
	@Test
	void devePermitirListarRestaurantes() {

		// Act
		List<Restaurante> restaurantesObtidos = listarRestaurantesUseCaseImpl.execute();
		

		//Assert
		assertThat(restaurantesObtidos)
				.isNotEmpty()
				.hasSizeGreaterThan(0);
		assertThat(restaurantesObtidos).allSatisfy(restaurante -> {
			assertThat(restaurante).isNotNull();
		});

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
