package br.com.fiap.restaurante.gateway.database.restauranteimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.gateway.database.entity.restaurante.RestauranteEntity;
import br.com.fiap.restaurante.gateway.database.repository.restaurante.RestauranteRepository;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

class RestauranteGatewayImplTest {
	
	RestauranteGateway restauranteGateway;
	
	@Mock
	private RestauranteRepository restauranteRepository;
	
	AutoCloseable openMocks;

	@BeforeEach
	void setup(){
		openMocks = MockitoAnnotations.openMocks(this);
		restauranteGateway = new RestauranteGatewayImpl(restauranteRepository);
	}

	@AfterEach
	void teardown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void devePermitirRegistrarRestaurante() {
		// Arrange
		Restaurante restaurante = gerarRestaurante();
		when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(gerarRestauranteEntity());
		
		// Act
		Restaurante retorno = restauranteGateway.salvar(restaurante);
		
		// Assert
		verify(restauranteRepository, times(1)).save(any(RestauranteEntity.class));
		assertThat(retorno).isInstanceOf(Restaurante.class).isNotNull();
	}
	
	@Test
	void devePermitirBuscarRestaurantePorId() {
        // Arrange
        Long id = 5L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(gerarRestauranteEntity()));

        // Act
        Optional<Restaurante> resultado = restauranteGateway.buscarPorId(id);

        // Assert
        verify(restauranteRepository, times(1)).findById(anyLong());
        assertThat(resultado).isPresent();
        assertThat(id).isEqualTo(resultado.get().getId());
        assertThat("Heroe's Burguer").isEqualTo(resultado.get().getNome());
	}
	
	@Test
	void devePermitirListarRestaurantes() {
        // Arrange
        List<RestauranteEntity> restaurantes = Arrays.asList(
                new RestauranteEntity(5L, "Heroe's Burguer", 
        				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15),
                new RestauranteEntity(6L, "Bistrô Gourmet", "Rua das Flores, 123 - Centro", "Francesa"
                		, "Das 12h às 20h - Seg a Sex.", 50)
            );
        when(restauranteRepository.findAll()).thenReturn(restaurantes);

        // Act
        List<Restaurante> retorno = restauranteGateway.listarTodos();

        // Assert
        verify(restauranteRepository, times(1)).findAll();
        assertThat(retorno).hasSize(2).allSatisfy(
        		restaurante -> {
        			assertThat(restaurante).isNotNull().isInstanceOf(Restaurante.class);
        		}
        );
        assertThat(restaurantes.get(1).getId()).isEqualTo(6L);
        assertThat(restaurantes.get(0).getNome()).isEqualTo("Heroe's Burguer");
        assertThat(restaurantes.get(1).getNome()).isEqualTo("Bistrô Gourmet");
	}
	
	@Test
	void devePermitirListarRestaurantes_retornaListaVaziaQuandoNenhumEncontrado() {
		// Arrange
        when(restauranteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Restaurante> retorno = restauranteGateway.listarTodos();

        // Assert
        verify(restauranteRepository, times(1)).findAll();
        assertThat(retorno).isEmpty();
	}
	
	@Test
	void devePermitirRemoverUmRestaurante() {
        // Arrange
        Long id = 1L;
        doNothing().when(restauranteRepository).deleteById(id);
        
        // Act
        restauranteGateway.deletar(id);

        // Assert
        verify(restauranteRepository, times(1)).deleteById(anyLong());
	}
	
	@Test
	void devePermitirAtualizarUmRestaurante() {
        // Arrange
		Restaurante restaurante = gerarRestaurante();
		Restaurante restauranteModificado = gerarRestaurante();
		restauranteModificado.setLocalizacao("Av. Salvador, 101");
		restauranteModificado.setTipoCozinha("Lanches Diversos");
		restauranteModificado.setCapacidade(30);
		when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(gerarRestauranteEntity());
        
        // Act
        Restaurante retorno = restauranteGateway.atualizar(restaurante, restauranteModificado);

        // Assert
        verify(restauranteRepository, times(1)).save(any(RestauranteEntity.class));
        assertThat(retorno).isNotNull();
		assertThat(retorno.getId()).isEqualTo(restaurante.getId());
		assertThat(retorno.getNome()).isEqualTo(restaurante.getNome());
		assertThat(retorno.getLocalizacao()).isNotEqualTo(restaurante.getLocalizacao());
		assertThat(retorno.getTipoCozinha()).isNotEqualTo(restaurante.getTipoCozinha());
		assertThat(retorno.getHorarioFuncionamento()).isEqualTo(restaurante.getHorarioFuncionamento());
		assertThat(retorno.getCapacidade()).isNotEqualTo(restaurante.getCapacidade());
        
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
	private RestauranteEntity gerarRestauranteEntity() {
		return new RestauranteEntity(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}
