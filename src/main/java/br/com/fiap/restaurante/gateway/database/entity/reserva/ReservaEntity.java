package br.com.fiap.restaurante.gateway.database.entity.reserva;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idClient;
    private Long idRestaurante;
    private Long totalPessoas;
    private LocalDateTime data;
    private Boolean confirmada;
}