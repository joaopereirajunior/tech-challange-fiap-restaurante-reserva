package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.ListarReservasUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarReservasUseCaseImpl implements ListarReservasUseCase {
    private final ReservaGateway reservaGateway;

    public ListarReservasUseCaseImpl(ReservaGateway reservaGateway) {
        this.reservaGateway = reservaGateway;
    }

    @Override
    public List<Reserva> execute() {
        return reservaGateway.listarTodos();
    }
}