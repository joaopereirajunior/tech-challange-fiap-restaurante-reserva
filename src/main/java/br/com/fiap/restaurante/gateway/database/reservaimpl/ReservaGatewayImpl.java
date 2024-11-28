package br.com.fiap.restaurante.gateway.database.reservaimpl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.exception.ClienteNaoEncontradoException;
import br.com.fiap.restaurante.exception.RestauranteNaoEncontradoException;
import br.com.fiap.restaurante.gateway.cliente.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.entity.reserva.ReservaEntity;
import br.com.fiap.restaurante.gateway.database.repository.reserva.ReservaRepository;
import br.com.fiap.restaurante.gateway.reserva.ReservaGateway;
import br.com.fiap.restaurante.gateway.restaurante.RestauranteGateway;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservaGatewayImpl implements ReservaGateway {
    private final ClienteGateway clienteGateway;
    private final ReservaRepository reservaRepository;
    private final RestauranteGateway restauranteGateway;

    //@Value("${restaurante.capacidadeReserva}")
	//private Long capacidadeReserva;

    public ReservaGatewayImpl(ReservaRepository reservaRepository, ClienteGateway clienteGateway, RestauranteGateway restauranteGateway) {
        this.reservaRepository = reservaRepository;
        this.clienteGateway = clienteGateway;
        this.restauranteGateway =  restauranteGateway;
    }

    @Override
    public Reserva salvar(Reserva reserva) {
        //restaurante.VerificarDisponibilidade(reserva.getData(), reserva.getTotalPessoas());
        var totalDisponibilidade = getTotalDisponibilidade(reserva);

        if(totalDisponibilidade == 0){
            throw new RuntimeException("Reserva indisponível, para a data: " + reserva.getData() + ", escolha uma outra data.");
        }

        if(totalDisponibilidade < reserva.getTotalPessoas()){
            throw new RuntimeException("Reserva indisponível, para a data: " + reserva.getData() + " temos disponibilidade de reserva para " + totalDisponibilidade + " pessoas.");
        }

        ReservaEntity entity = new ReservaEntity();
        entity.setId(reserva.getId());
        entity.setIdClient(reserva.getCliente().getId());
        entity.setIdRestaurante(reserva.getRestaurante().getId());
        entity.setTotalPessoas(reserva.getTotalPessoas());
        entity.setData(reserva.getData());
        entity.setConfirmada(reserva.getConfirmada());

        ReservaEntity savedEntity = reservaRepository.save(entity);
        return entityToDomain(savedEntity);
    }

    @Override
    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id).map(entity -> entityToDomain(entity));
    }

    @Override
    public List<Reserva> listarTodos() {
        return reservaRepository.findAll().stream().map(entity -> entityToDomain(entity)).collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }

    private Reserva entityToDomain(ReservaEntity entity)
    {
        return new Reserva(
            clienteGateway.buscarPorId(entity.getIdClient()).orElseThrow(() -> new ClienteNaoEncontradoException(entity.getIdClient())),
            restauranteGateway.buscarPorId(entity.getIdRestaurante()).orElseThrow(() -> new RestauranteNaoEncontradoException(entity.getIdRestaurante())),
            entity.getId(),
            entity.getTotalPessoas(),
            entity.getData(),
            entity.getConfirmada());
    }

    private Long getTotalDisponibilidade(Reserva reserva)
    {
        var restaurante = restauranteGateway.buscarPorId(reserva.getRestaurante().getId());

        if(!restaurante.isPresent()){
            throw new RestauranteNaoEncontradoException(reserva.getRestaurante().getId());
        }

        var totalReservado = reservaRepository.findAll().stream().filter(r -> r.getData().equals(reserva.getData())).collect(Collectors.toList()).stream().mapToLong(t -> t.getTotalPessoas()).sum();

        return (restaurante.get().getCapacidade() - totalReservado);
    }
}
