package br.com.fiap.restaurante.gateway.database;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.gateway.ClienteGateway;
import br.com.fiap.restaurante.gateway.database.entity.ClienteEntity;
import br.com.fiap.restaurante.gateway.database.repository.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClienteGatewayImpl implements ClienteGateway {
    private final ClienteRepository clienteRepository;

    public ClienteGatewayImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNome(cliente.getNome());
        entity.setCpf(cliente.getCpf());
        ClienteEntity savedEntity = clienteRepository.save(entity);
        return new Cliente(savedEntity.getId(), savedEntity.getNome(), savedEntity.getCpf());

    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(entity -> new Cliente(entity.getId(), entity.getNome(), entity.getCpf()));
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(entity -> new Cliente(entity.getId(), entity.getNome(), entity.getCpf()))
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {

        clienteRepository.deleteById(id);
    }
}
