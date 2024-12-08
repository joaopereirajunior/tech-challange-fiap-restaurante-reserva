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
    public Boolean finalizada;
    public Long notaAvaliacao;
    public String comentarioAvaliacao;

    public void setNotaAvaliacao(Long notaAvaliacao){
        if(notaAvaliacao > 5L){
            this.notaAvaliacao = 5L;
            return;
        }

        if(notaAvaliacao < 0){
            this.notaAvaliacao = 0L;
            return;
        }
        
        this.notaAvaliacao = notaAvaliacao;
    }
}
