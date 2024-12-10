package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.FinalizarReservaUseCase;

import org.springframework.stereotype.Service;

@Service
public class FinalizarReservaUseCaseImpl implements FinalizarReservaUseCase {
    private final ReservaGateway reservaGateway;

    public FinalizarReservaUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Reserva execute(Long id, Reserva reserva) {
        return reservaGateway.finalizar(id, reserva);
    }
}