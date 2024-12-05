package br.com.fiap.restaurante.usecase.restaurante.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class ListarRestaurantesUseCaseImplTest {

	private ListarRestaurantesUseCaseImpl listarRestaurantesUseCaseImpl;
	
	@Mock
	RestauranteGateway restauranteGateway;
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		listarRestaurantesUseCaseImpl = new ListarRestaurantesUseCaseImpl(restauranteGateway);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirListarRestaurantes() {
		// Arrange
		List<Restaurante> listaRestaurantes = gerarRestaurantes();
		when(restauranteGateway.listarTodos()).thenReturn(listaRestaurantes);
		
		// Act
		List<Restaurante> restaurantesObtidos = listarRestaurantesUseCaseImpl.execute();
		
		// Assert
		verify(restauranteGateway, times(1)).listarTodos();
		assertThat(restaurantesObtidos).hasSize(3);
		assertThat(restaurantesObtidos).allSatisfy( restaurante -> {
			assertThat(restaurante).isNotNull().isInstanceOf(Restaurante.class);
		});

	}
	
	private List<Restaurante> gerarRestaurantes() {
		List<Restaurante> listaRestaurantes = Arrays.asList(
			new Restaurante(1L, "Pizza Master", 
		                "Avenida Paulista, 1000", "Pizzas e Massas", "Das 11h às 23h - Todos os dias", 50),

			new Restaurante(2L, "Sushi House", 
			                "Rua dos Três Irmãos, 45", "Sushis e Comida Japonesa", "Das 12h às 22h - Seg a Sáb.", 30),
	
			new Restaurante(3L, "Taco Loco", 
			                "Rua das Flores, 120", "Comida Mexicana", "Das 10h às 20h - Todos os dias", 40)
		);
		return listaRestaurantes;
	}
}
