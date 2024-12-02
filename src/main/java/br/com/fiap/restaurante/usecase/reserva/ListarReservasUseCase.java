package br.com.fiap.restaurante.usecase.reserva;

import br.com.fiap.restaurante.domain.Reserva;
import java.util.List;

public interface ListarReservasUseCase {
    List<Reserva> execute();
}