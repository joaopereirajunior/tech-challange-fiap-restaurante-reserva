package br.com.fiap.restaurante.usecase.reserva.impl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.usecase.reserva.AtualizarReservaUseCase;
import br.com.fiap.restaurante.usecase.reserva.ObterReservaPorIdUseCase;
import org.springframework.stereotype.Service;

@Service
public class AtualizarReservaUseCaseImpl implements AtualizarReservaUseCase {
    private final ReservaGateway reservaGateway;
    private final ObterReservaPorIdUseCase buscarReservaPorIdUseCase;

    public AtualizarReservaUseCaseImpl(ReservaGateway reservaGateway, ObterReservaPorIdUseCase buscarReservaPorIdUseCase) {
        this.reservaGateway = reservaGateway;
        this.buscarReservaPorIdUseCase = buscarReservaPorIdUseCase;
    }

    @Override
    public Reserva execute(Long id, Reserva reserva) {
        var reservaExistente = buscarReservaPorIdUseCase.execute(id); // Verifica se existe
        if(reservaExistente == null){
            throw new ReservaNaoEncontradaException(id);
        }
        reserva.setId(id);
        return reservaGateway.salvar(reserva);
    }
}