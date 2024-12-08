package br.com.fiap.restaurante.usecase.reserva;
import java.util.Optional;

import br.com.fiap.restaurante.domain.Reserva;

public interface ObterReservaPorIdUseCase {
    Optional<Reserva> execute(Long id);
}