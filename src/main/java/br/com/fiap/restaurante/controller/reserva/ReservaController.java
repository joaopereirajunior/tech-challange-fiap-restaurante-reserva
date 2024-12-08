//package br.com.fiap.restaurante.controller;
//public class ReservaController
package br.com.fiap.restaurante.controller.reserva;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.usecase.reserva.AtualizarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.ObterReservaPorIdUseCase;
import br.com.fiap.restaurante.usecase.reserva.CriarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.DeletarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.ListarReservasUseCase;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservas")
public class ReservaController {
    private final CriarReservaUseCase efetuarReservaUseCase;
    private final ObterReservaPorIdUseCase buscarReservaPorIdUseCase;
    private final ListarReservasUseCase listarReservasUseCase;
    private final AtualizarReservaUseCase atualizarReservaUseCase;
    private final DeletarReservaUseCase excluirReservaUseCase;

    @PostMapping
    public ResponseEntity<Reserva> efetuarReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(efetuarReservaUseCase.execute(reserva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Reserva>> obterReservaById(@PathVariable Long id) {
        return ResponseEntity.ok(buscarReservaPorIdUseCase.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(listarReservasUseCase.execute());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        return ResponseEntity.ok(atualizarReservaUseCase.execute(id, reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        excluirReservaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}