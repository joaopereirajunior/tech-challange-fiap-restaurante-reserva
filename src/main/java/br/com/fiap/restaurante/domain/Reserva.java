package br.com.fiap.restaurante.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reserva {
    public Cliente cliente;
    public Restaurante restaurante;
    public Long id;
    public Long totalPessoas;
    public LocalDateTime data;
    public Boolean confirmada;
}
