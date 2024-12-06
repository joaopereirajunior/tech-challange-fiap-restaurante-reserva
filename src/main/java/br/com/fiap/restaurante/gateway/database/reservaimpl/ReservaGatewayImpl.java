package br.com.fiap.restaurante.gateway.database.reservaimpl;

import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
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
        var retorno = mapToDomain(savedEntity);

        retorno.setCliente(reserva.getCliente());
        retorno.setRestaurante(reserva.getRestaurante());

        return retorno;
    }

    public Reserva atualizar(Reserva reservaRecuperada, Reserva reservaModificada){

        reservaRecuperada.setCliente(reservaModificada.getCliente());
        reservaRecuperada.setConfirmada(reservaModificada.getConfirmada());
        reservaRecuperada.setData(reservaModificada.getData());
        reservaRecuperada.setRestaurante(reservaModificada.getRestaurante());
        reservaRecuperada.setTotalPessoas(reservaModificada.getTotalPessoas());

        var entity = mapToEntity(reservaRecuperada);

        reservaRepository.save(entity);

        var retorno = mapToDomain(entity);

        retorno.setCliente(reservaModificada.getCliente());
        retorno.setRestaurante(reservaModificada.getRestaurante());

        return retorno;
    }

    @Override
    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id).map(entity -> mapToDomain(entity));
    }

    @Override
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll().stream().map(entity -> mapToDomain(entity)).collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        var reserva = buscarPorId(id);

        if(!reserva.isPresent()){
            throw new ReservaNaoEncontradaException(id);
        }

        reservaRepository.deleteById(id);
    }

    private Reserva mapToDomain(ReservaEntity entity)
    {
        var cliente = clienteGateway.buscarPorId(entity.getIdClient());
        var restaurante =restauranteGateway.buscarPorId(entity.getIdRestaurante());

        var reserva = new Reserva(
            null,
            null,
            entity.getId(),
            entity.getTotalPessoas(),
            entity.getData(),
            entity.getConfirmada());

        if(cliente.isPresent()){
            reserva.setCliente(cliente.get());
        }

        if(restaurante.isPresent()){
            reserva.setRestaurante(restaurante.get());
        }

        return reserva;
    }

    private ReservaEntity mapToEntity(Reserva reserva)
    {
        ReservaEntity entity = new ReservaEntity();
        entity.setId(reserva.getId());
        entity.setIdClient(reserva.getCliente().getId());
        entity.setIdRestaurante(reserva.getRestaurante().getId());
        entity.setTotalPessoas(reserva.getTotalPessoas());
        entity.setData(reserva.getData());
        entity.setConfirmada(reserva.getConfirmada());

        return entity;
    }

    private Long getTotalDisponibilidade(Reserva reserva)
    {
        var restaurante = Optional.of(reserva.getRestaurante());

        if(!restaurante.isPresent()){
            throw new RestauranteNaoEncontradoException(reserva.getRestaurante().getId());
        }

        if(reserva.getRestaurante().getCapacidade() == 0){
            restaurante = restauranteGateway.buscarPorId(reserva.getRestaurante().getId());
        }

        var totalReservado = reservaRepository.findAll().stream().filter(r -> r.getData().equals(reserva.getData())).collect(Collectors.toList()).stream().mapToLong(t -> t.getTotalPessoas()).sum();

        return (restaurante.get().getCapacidade() - totalReservado);
    }
}
