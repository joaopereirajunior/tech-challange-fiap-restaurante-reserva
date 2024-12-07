package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.AtualizarReservaUseCase;
import org.springframework.stereotype.Service;

@Service
public class AtualizarReservaUseCaseImpl implements AtualizarReservaUseCase {
    private final ReservaGateway reservaGateway;

    public AtualizarReservaUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Reserva execute(Long id, Reserva reservaModificada) {
        return reservaGateway.atualizar(id, reservaModificada);
    }
}