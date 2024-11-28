package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.BuscarReservaPorIdUseCase;
import org.springframework.stereotype.Service;


@Service
public class BuscarReservaPorIdUseCaseImpl implements BuscarReservaPorIdUseCase {
    private final ReservaGateway reservaGateway;

    public BuscarReservaPorIdUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public Reserva execute(Long id) {
        return reservaGateway.buscarPorId(id).orElseThrow(() -> new ReservaNaoEncontradaException(id));
    }
}