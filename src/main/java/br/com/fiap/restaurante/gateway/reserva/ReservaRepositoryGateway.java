package br.com.fiap.restaurante.gateway.reserva;

import br.com.fiap.restaurante.domain.Reserva;
//import org.springframework.stereotype.Repository;
import java.util.List;
public interface ReservaRepositoryGateway {
    Reserva salvar(Reserva reserva);

    Reserva atualizar(Long id, Reserva reservaModificada);

    Reserva buscarPorId(Long id);

    List<Reserva> listarTodas();

    Reserva deletar(Long id);
}
