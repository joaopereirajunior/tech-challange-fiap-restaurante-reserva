package br.com.fiap.restaurante.gateway.database.reservaimpl;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.exception.ReservaNaoEncontradaException;
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

    public ReservaGatewayImpl(ReservaRepository reservaRepository, ClienteGateway clienteGateway, RestauranteGateway restauranteGateway) {
        this.reservaRepository = reservaRepository;
        this.clienteGateway = clienteGateway;
        this.restauranteGateway =  restauranteGateway;
    }

    @Override
    public Reserva salvar(Reserva reserva) {
        Long totalDisponibilidade = getTotalDisponibilidade(reserva);

        if(totalDisponibilidade < reserva.getTotalPessoas()){
            throw new RuntimeException("Reserva indisponível, para a data selecionada, escolha uma outra data.");
        }

        ReservaEntity entity = new ReservaEntity();
        entity.setId(reserva.getId());
        entity.setIdClient(reserva.getCliente().getId());
        entity.setIdRestaurante(reserva.getRestaurante().getId());
        entity.setTotalPessoas(reserva.getTotalPessoas());
        entity.setData(reserva.getData());
        entity.setConfirmada(reserva.getConfirmada());
        entity.setFinalizada(reserva.getFinalizada());
		entity.setNotaAvaliacao(reserva.getNotaAvaliacao());
		entity.setComentarioAvaliacao(reserva.getComentarioAvaliacao());

        ReservaEntity savedEntity = reservaRepository.save(entity);
        Reserva retorno = mapToDomain(savedEntity);

        retorno.setCliente(reserva.getCliente());
        retorno.setRestaurante(reserva.getRestaurante());

        return retorno;
    }

    public Reserva atualizar(Long id, Reserva reservaModificada){
        Optional<Reserva> reservaRecuperadaOptional = this.buscarPorId(id);

        if(reservaRecuperadaOptional.isEmpty()){
            throw new ReservaNaoEncontradaException(id);
        }

        var reservaRecuperada = reservaRecuperadaOptional.get();
        
        reservaRecuperada.setCliente(reservaModificada.getCliente());
        reservaRecuperada.setConfirmada(reservaModificada.getConfirmada());
        reservaRecuperada.setData(reservaModificada.getData());
        reservaRecuperada.setRestaurante(reservaModificada.getRestaurante());
        reservaRecuperada.setTotalPessoas(reservaModificada.getTotalPessoas());
        reservaRecuperada.setFinalizada(reservaModificada.getFinalizada());
		reservaRecuperada.setNotaAvaliacao(reservaModificada.getNotaAvaliacao());
		reservaRecuperada.setComentarioAvaliacao(reservaModificada.getComentarioAvaliacao());

        ReservaEntity entity = mapToEntity(reservaModificada);

        reservaRepository.save(entity);

        Reserva retorno = mapToDomain(entity);

        retorno.setCliente(reservaModificada.getCliente());
        retorno.setRestaurante(reservaModificada.getRestaurante());

        return retorno;
    }

    @Override
    public Optional<Reserva> buscarPorId(Long id) {

        Optional<ReservaEntity> entity = reservaRepository.findById(id);

        return entity.map(item -> mapToDomain(item));
    }

    @Override
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll().stream().map(entity -> mapToDomain(entity)).collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public Reserva finalizar(Long id, Reserva reserva){
        reserva.setFinalizada(true);

        return atualizar(id, reserva);
    }
    private Reserva mapToDomain(ReservaEntity entity)
    {
        Optional<Cliente> cliente = clienteGateway.buscarPorId(entity.getIdClient());
        Optional<Restaurante> restaurante = restauranteGateway.buscarPorId(entity.getIdRestaurante());

        Reserva reserva = new Reserva(
            null,
            null,
            entity.getId(),
            entity.getTotalPessoas(),
            entity.getData(),
            entity.getConfirmada(),
            entity.getFinalizada(),
            entity.getNotaAvaliacao(),
            entity.getComentarioAvaliacao());

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
        entity.setFinalizada(reserva.getFinalizada());
		entity.setNotaAvaliacao(reserva.getNotaAvaliacao());
		entity.setComentarioAvaliacao(reserva.getComentarioAvaliacao());
        return entity;
    }

    private Long getTotalDisponibilidade(Reserva reserva)
    {
        Restaurante restaurante = reserva.getRestaurante();

        Long totalReservado = reservaRepository.findAll().stream().filter(r -> r.getData().equals(reserva.getData())).collect(Collectors.toList()).stream().mapToLong(t -> t.getTotalPessoas()).sum();

        return (restaurante.getCapacidade() - totalReservado);
    }
}
