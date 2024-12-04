package br.com.fiap.restaurante.gateway.reserva;

import br.com.fiap.restaurante.domain.Reserva;
//import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
public interface ReservaRepositoryGateway {
    Reserva salvar(Reserva reserva);

    Reserva atualizar(Reserva reserva, Reserva reservaModificada);

    Optional<Reserva> buscarPorId(Long id);

    List<Reserva> listarTodas();

    void deletar(Long id);
}
