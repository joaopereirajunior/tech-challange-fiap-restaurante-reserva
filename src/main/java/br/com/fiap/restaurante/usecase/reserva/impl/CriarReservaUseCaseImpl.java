package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.CriarReservaUseCase;

import org.springframework.stereotype.Service;

@Service
public class CriarReservaUseCaseImpl implements CriarReservaUseCase {
    private final ReservaGateway reservaGateway;

    public CriarReservaUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Reserva execute(Reserva reserva) {
        return reservaGateway.salvar(reserva);
    }
}