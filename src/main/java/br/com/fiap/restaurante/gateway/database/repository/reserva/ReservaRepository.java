package br.com.fiap.restaurante.gateway.database.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.restaurante.gateway.database.entity.reserva.ReservaEntity;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
}