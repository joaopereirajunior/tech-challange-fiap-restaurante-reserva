package br.com.fiap.restaurante.usecase.reserva;
import br.com.fiap.restaurante.domain.Reserva;

public interface FinalizarReservaUseCase {
    Reserva execute(Long id, Reserva reserva);
}