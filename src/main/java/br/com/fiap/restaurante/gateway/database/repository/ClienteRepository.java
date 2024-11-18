package br.com.fiap.restaurante.gateway.database.repository;

import br.com.fiap.restaurante.gateway.database.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
