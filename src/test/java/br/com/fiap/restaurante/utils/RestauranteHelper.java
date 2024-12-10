package br.com.fiap.restaurante.utils;

import java.time.LocalDateTime;

import br.com.fiap.restaurante.domain.Cliente;
import br.com.fiap.restaurante.domain.Reserva;
import br.com.fiap.restaurante.domain.Restaurante;

public class RestauranteHelper {
    
    public static Cliente gerarClienteRequest() {
        return new Cliente(1l, "João da Silva", "123.456.789-00");
    }

    public static Restaurante gerarRestauranteRequest() {
        return new Restaurante(1l, "Heroe's Burguer", 
				"Rua de Teste, 59", "Hamburguers e Lanches", "Das 9h às 18h - Seg a Sex.", 150);
    }

    public static Reserva gerarReservaRequest() {
        var cliente = gerarClienteRequest();
		
		var restaurante = gerarRestauranteRequest();

		return new Reserva(cliente, restaurante, 1l, 10, LocalDateTime.now(), false, false, 0, null);
    }

}
