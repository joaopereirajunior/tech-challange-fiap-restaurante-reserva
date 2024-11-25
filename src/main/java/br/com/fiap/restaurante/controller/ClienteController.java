package br.com.fiap.restaurante.controller;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.usecase.cliente.CriarClienteUseCase;
import br.com.fiap.restaurante.usecase.cliente.ObterClientePorIdUseCase;
import br.com.fiap.restaurante.usecase.cliente.ObterClienteUseCase;
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

    public ClienteController(CriarClienteUseCase criarClienteUseCase,ObterClienteUseCase obterClienteUseCase, ObterClientePorIdUseCase obterClientePorIdUseCase) {

        this.criarClienteUseCase = criarClienteUseCase;
        this.obterClienteUseCase = obterClienteUseCase;
        this.obterClientePorIdUseCase = obterClientePorIdUseCase;

    }

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(criarClienteUseCase.execute(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Cliente>> obterClientePorId(@PathVariable Long id) {
        return ResponseEntity.ok(obterClientePorIdUseCase.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obterClientes() {
        return ResponseEntity.ok(obterClienteUseCase.execute());
    }

   /* @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }*/
}
