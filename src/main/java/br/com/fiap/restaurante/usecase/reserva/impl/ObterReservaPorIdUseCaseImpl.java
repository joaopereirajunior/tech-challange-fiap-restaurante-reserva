package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.ObterReservaPorIdUseCase;

import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public class ObterReservaPorIdUseCaseImpl implements ObterReservaPorIdUseCase {
    private final ReservaGateway reservaGateway;

    public ObterReservaPorIdUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Optional<Reserva> execute(Long id) {
        return reservaGateway.buscarPorId(id);
    }
}