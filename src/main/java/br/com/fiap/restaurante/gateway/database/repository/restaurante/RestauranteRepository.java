package br.com.fiap.restaurante.gateway.database.repository.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.restaurante.gateway.database.entity.restaurante.RestauranteEntity;

public interface RestauranteRepository extends JpaRepository<RestauranteEntity, Long> {

}
