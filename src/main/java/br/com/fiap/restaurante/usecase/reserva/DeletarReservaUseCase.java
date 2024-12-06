package br.com.fiap.restaurante.usecase.reserva;

import br.com.fiap.restaurante.domain.Reserva;

public interface DeletarReservaUseCase {
    Reserva execute(Long id);
}