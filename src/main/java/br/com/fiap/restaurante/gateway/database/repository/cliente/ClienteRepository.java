package br.com.fiap.restaurante.gateway.database.repository.cliente;

import br.com.fiap.restaurante.gateway.database.entity.cliente.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
