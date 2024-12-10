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
    public int totalPessoas;
    public LocalDateTime data;
    public Boolean confirmada;
    public Boolean finalizada;
    public int notaAvaliacao;
    public String comentarioAvaliacao;

    public void setNotaAvaliacao(int notaAvaliacao){
        if(notaAvaliacao > 5){
            this.notaAvaliacao = 5;
            return;
        }

        if(notaAvaliacao < 0){
            this.notaAvaliacao = 0;
            return;
        }
        
        this.notaAvaliacao = notaAvaliacao;
    }
}
