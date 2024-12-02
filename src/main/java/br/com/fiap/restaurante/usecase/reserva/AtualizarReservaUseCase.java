package br.com.fiap.restaurante.usecase.reserva;
import br.com.fiap.restaurante.domain.Reserva;

public interface AtualizarReservaUseCase {
    Reserva execute(Long id, Reserva reserva);
}