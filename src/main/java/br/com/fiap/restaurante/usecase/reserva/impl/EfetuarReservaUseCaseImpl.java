package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.EfetuarReservaUseCase;

import org.springframework.stereotype.Service;

@Service
public class EfetuarReservaUseCaseImpl implements EfetuarReservaUseCase {
    private final ReservaGateway reservaGateway;

    public EfetuarReservaUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Reserva execute(Reserva reserva) {
        return reservaGateway.salvar(reserva);
    }
}