package br.com.fiap.restaurante.gateway.database.entity.reserva;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class ReservaEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idClient;
    private Long idRestaurante;
    private Long totalPessoas;
    private LocalDateTime data;
    private Boolean confirmada;
    private Boolean finalizada;
    private Long notaAvaliacao;
    private String comentarioAvaliacao;
}