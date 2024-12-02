package br.com.fiap.restaurante.usecase.reserva;
import br.com.fiap.restaurante.domain.Reserva;

public interface BuscarReservaPorIdUseCase {
    Reserva execute(Long id);
}