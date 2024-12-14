package br.com.fiap.restaurante.controller.cliente;


import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.usecase.cliente.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClienteControllerTest {

    private MockMvc mockMvc;

    AutoCloseable mock;

    @Mock
    private CriarClienteUseCase criarClienteUseCase;

    @Mock
    private ObterClientePorIdUseCase obterClientePorIdUseCase;

    @Mock
    private ObterClienteUseCase obterClienteUseCase;

    @Mock
    private AlterarClienteUseCase alterarClienteUseCase;

    @Mock
    private DeletarClienteUseCase deletarCliente;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .addFilter((request, response, chain) -> {response.setCharacterEncoding("UTF-8"); chain
                        .doFilter(request, response);
                })
                .build();

    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Test
    public void devePermitirCriarCliente() throws Exception
    {

        var cliente = new Cliente(1L, "Joao","12345678901");

        when(criarClienteUseCase.execute(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Joao"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));


    }


    @Test
    public void devePermitirObterClientePorId() throws Exception
    {
        var id = 1L;
        var cliente = new Cliente(1L, "Joao","12345678901");

        when(obterClientePorIdUseCase.execute(anyLong())).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Joao"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));

    }

    @Test
    public void devePermitirObterCliente() throws Exception
    {
        var cliente = new Cliente(1L, "Joao","12345678901");

        List<Cliente> clientes = Arrays.asList(cliente);

        when(obterClienteUseCase.execute()).thenReturn(java.util.List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Joao"))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"));

    }

    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        Long id = 1L;
        var clienteAtualizado = new Cliente(1L, "Joao","12345678901");
        clienteAtualizado.setNome("Joao Silva");
        clienteAtualizado.setCpf("12345678902");
        when(alterarClienteUseCase.execute(anyLong(), any(Cliente.class))).thenReturn(clienteAtualizado);

        mockMvc.perform(put("/clientes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Joao Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678902"));

    }

    @Test
    void deveRemoverClienteComSucesso() throws Exception {
        doNothing().when(deletarCliente).execute(1L);

        mockMvc.perform(delete("/clientes/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
