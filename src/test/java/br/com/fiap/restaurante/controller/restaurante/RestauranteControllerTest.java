package br.com.fiap.restaurante.controller.restaurante;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.usecase.restaurante.AtualizarRestauranteUseCase;
import br.com.fiap.restaurante.usecase.restaurante.CriarRestauranteUseCase;
import br.com.fiap.restaurante.usecase.restaurante.ListarRestaurantesUseCase;
import br.com.fiap.restaurante.usecase.restaurante.ObterRestaurantePorIdUseCase;
import br.com.fiap.restaurante.usecase.restaurante.RemoverRestauranteUseCase;

class RestauranteControllerTest {

	private MockMvc mockMvc;
	
	AutoCloseable mock;

    @Mock
    private CriarRestauranteUseCase criarRestauranteUseCase;

    @Mock
    private ListarRestaurantesUseCase listarRestaurantesUseCase;

    @Mock
    private ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase;

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Mock
    private RemoverRestauranteUseCase removerRestauranteUseCase;

    @InjectMocks
    private RestauranteController restauranteController;

    @BeforeEach
    void setUp() {
    	mock = MockitoAnnotations.openMocks(this);
    	mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
    		.addFilter((request, response, chain) -> {
    			response.setCharacterEncoding("UTF-8");
    			chain.doFilter(request, response);
    		})
    		.build();
    }
    
    @AfterEach
    void tearDown() throws Exception {
    	mock.close();
    }
    
    @Test
    void devePermitirCriarRestaurante() throws Exception {
    	
    	Restaurante restaurante = gerarRestaurante();
    	
        when(criarRestauranteUseCase.execute(any())).thenReturn(restaurante);

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(restaurante)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nome").value("Heroe's Burguer"))
                .andExpect(jsonPath("$.localizacao").value("Rua de Teste, 59"));
    }
    
    @Test
    void deveListarRestaurantesComSucesso() throws Exception {
    	List<Restaurante> restaurantes = gerarListaRestaurantes();
        when(listarRestaurantesUseCase.execute()).thenReturn(restaurantes);

        mockMvc.perform(get("/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nome").value("Pizza Master"))
                .andExpect(jsonPath("$[1].nome").value("Sushi House"));
    }

    @Test
    void deveObterRestaurantePorIdComSucesso() throws Exception {
    	Long id = 5L;
        Restaurante restaurante = gerarRestaurante();
        when(obterRestaurantePorIdUseCase.execute(anyLong())).thenReturn(restaurante);

        mockMvc.perform(get("/restaurantes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nome").value("Heroe's Burguer"))
                .andExpect(jsonPath("$.localizacao").value("Rua de Teste, 59"));
    }

    @Test
    void deveAtualizarRestauranteComSucesso() throws Exception {
    	Long id = 5L;
        Restaurante restauranteAtualizado = gerarRestaurante();
        restauranteAtualizado.setLocalizacao("Av. Salvador, 101");
        restauranteAtualizado.setTipoCozinha("Lanches Diversos");
        restauranteAtualizado.setCapacidade(30);
        when(atualizarRestauranteUseCase.execute(anyLong(), any(Restaurante.class))).thenReturn(restauranteAtualizado);

        mockMvc.perform(put("/restaurantes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(restauranteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nome").value("Heroe's Burguer"))
                .andExpect(jsonPath("$.localizacao").value("Av. Salvador, 101"))
                .andExpect(jsonPath("$.tipoCozinha").value("Lanches Diversos"))
                .andExpect(jsonPath("$.capacidade").value(30));
    }

    @Test
    void deveRemoverRestauranteComSucesso() throws Exception {
        doNothing().when(removerRestauranteUseCase).execute(1L);

        mockMvc.perform(delete("/restaurantes/1"))
                .andExpect(status().isNoContent());
    }
    
	public static String asJsonString(final Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
	
	private List<Restaurante> gerarListaRestaurantes() {
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
