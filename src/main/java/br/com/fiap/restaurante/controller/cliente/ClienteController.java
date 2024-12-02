package br.com.fiap.restaurante.controller.cliente;

import br.com.fiap.restaurante.controller.cliente.dto.ClienteRequestDTO;
import br.com.fiap.restaurante.controller.cliente.mapper.ClienteMapper;
import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.usecase.cliente.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final CriarClienteUseCase criarClienteUseCase;
    private final ObterClienteUseCase obterClienteUseCase;
    private final ObterClientePorIdUseCase obterClientePorIdUseCase;
    private final AlterarClienteUseCase alterarClienteUseCase;
    private final DeletarClienteUseCase deletarCliente;


    public ClienteController(CriarClienteUseCase criarClienteUseCase, ObterClienteUseCase obterClienteUseCase, ObterClientePorIdUseCase obterClientePorIdUseCase, AlterarClienteUseCase alterarClienteUseCase, DeletarClienteUseCase deletarCliente)
    {
        this.criarClienteUseCase = criarClienteUseCase;
        this.obterClienteUseCase = obterClienteUseCase;
        this.obterClientePorIdUseCase = obterClientePorIdUseCase;
        this.alterarClienteUseCase = alterarClienteUseCase;
        this.deletarCliente = deletarCliente;
    }

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        var cliente = ClienteMapper.toDomain(clienteRequestDTO);

        return ResponseEntity.ok(criarClienteUseCase.execute(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Cliente>> obterClientePorId(@PathVariable Long id) {
        return ResponseEntity.ok(obterClientePorIdUseCase.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obterCliente() {
        return ResponseEntity.ok(obterClienteUseCase.execute());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(alterarClienteUseCase.execute(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        deletarCliente.execute(id);
        return ResponseEntity.noContent().build();
    }
}
