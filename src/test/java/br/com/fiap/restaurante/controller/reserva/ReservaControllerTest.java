package br.com.fiap.restaurante.controller.reserva;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.usecase.reserva.AtualizarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.CriarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.ListarReservasUseCase;
import br.com.fiap.restaurante.usecase.reserva.ObterReservaPorIdUseCase;
import br.com.fiap.restaurante.usecase.reserva.DeletarReservaUseCase;

class ReservaControllerTest {

	private MockMvc mockMvc;
	
	AutoCloseable mock;

    @Mock
    private CriarReservaUseCase criarReservaUseCase;

    @Mock
    private ListarReservasUseCase listarReservasUseCase;

    @Mock
    private ObterReservaPorIdUseCase obterReservaPorIdUseCase;

    @Mock
    private AtualizarReservaUseCase atualizarReservaUseCase;

    @Mock
    private DeletarReservaUseCase deletarReservaUseCase;

    @InjectMocks
    private ReservaController reservaController;

    @BeforeEach
    void setUp() {
    	mock = MockitoAnnotations.openMocks(this);
    	mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
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
    void devePermitirCriarReserva() throws Exception {
    	
    	Reserva reserva = gerarReserva(5L);
    	
        when(criarReservaUseCase.execute(any())).thenReturn(reserva);

        var content = asJsonString(reserva);

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.confirmada").value(false))
                .andExpect(jsonPath("$.totalPessoas").value(10L));
    }
    
    @Test
    void deveListarReservasComSucesso() throws Exception {
    	List<Reserva> reservas = gerarListaReservas();
        when(listarReservasUseCase.execute()).thenReturn(reservas);

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].confirmada").value(false))
                .andExpect(jsonPath("$[1].totalPessoas").value(10));
    }

    @Test
    void deveObterReservaPorIdComSucesso() throws Exception {
    	Long id = 5L;
        Reserva reserva = gerarReserva(id);
        when(obterReservaPorIdUseCase.execute(anyLong())).thenReturn(reserva);

        mockMvc.perform(get("/reservas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.confirmada").value(false))
                .andExpect(jsonPath("$.totalPessoas").value(10L));
    }

    @Test
    void deveAtualizarReservaComSucesso() throws Exception {
    	Long id = 5L;
        Reserva reservaAtualizada = gerarReserva(id);
        reservaAtualizada.setConfirmada(true);
        reservaAtualizada.setTotalPessoas(20L);
        
        when(atualizarReservaUseCase.execute(anyLong(), any(Reserva.class))).thenReturn(reservaAtualizada);

        var content = asJsonString(reservaAtualizada);

        mockMvc.perform(put("/reservas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.confirmada").value(true))
                .andExpect(jsonPath("$.totalPessoas").value(20L));
    }

    @Test
    void deveDeletarReservaComSucesso() throws Exception {
        var reserva = gerarReserva(1L);

        when(deletarReservaUseCase.execute(anyLong())).thenReturn(reserva);

        mockMvc.perform(delete("/reservas/1")).andExpect(status().isNoContent());
    }
    
	public static String asJsonString(final Reserva object) {
		try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			return "{ }";
		}
	}

	private List<Reserva> gerarListaReservas() {
		List<Reserva> listaReservas = Arrays.asList(
			gerarReserva(1L),
			gerarReserva(2L),
			gerarReserva(3L)
		);
		return listaReservas;
	}

	private Reserva gerarReserva(Long id) {
		var cliente = new Cliente(1l, "Juca das Rosas", "07406565940");
		var restaurante = gerarRestaurante();
		return new Reserva(cliente, restaurante, id, 10L, LocalDateTime.now(), false);
	}
	
	private Restaurante gerarRestaurante() {
		return new Restaurante(5L, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 15);
	}
}