package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.DeletarReservaUseCase;

import org.springframework.stereotype.Service;

@Service
public class DeletarReservaUseCaseImpl implements DeletarReservaUseCase {
    private final ReservaGateway reservaGateway;

    public DeletarReservaUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public void execute(Long id) {
        var reserva = reservaGateway.buscarPorId(id);
        if (!reserva.isPresent()) {
            throw new ReservaNaoEncontradaException(id);
        }
        reservaGateway.deletar(id);
    }
}