package br.com.fiap.restaurante.controller.restaurante;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.restaurante.domain.Restaurante;
import br.com.fiap.restaurante.usecase.restaurante.AtualizarRestauranteUseCase;
import br.com.fiap.restaurante.usecase.restaurante.CriarRestauranteUseCase;
import br.com.fiap.restaurante.usecase.restaurante.ListarRestaurantesUseCase;
import br.com.fiap.restaurante.usecase.restaurante.ObterRestaurantePorIdUseCase;
import br.com.fiap.restaurante.usecase.restaurante.RemoverRestauranteUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	private final CriarRestauranteUseCase criarRestauranteUseCase;
	private final ListarRestaurantesUseCase listarRestaurantesUseCase;
	private final ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase;
	private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
	private final RemoverRestauranteUseCase removerRestauranteUseCase;
	
    @PostMapping
    public ResponseEntity<Restaurante> criarRestaurante(@RequestBody Restaurante restaurante) {
        return ResponseEntity.ok(criarRestauranteUseCase.execute(restaurante));
    }

    @GetMapping
    public ResponseEntity<List<Restaurante>> listarRestaurantes() {
        return ResponseEntity.ok(listarRestaurantesUseCase.execute());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Restaurante>> obterRestaurantePorId(@PathVariable Long id) {
        return ResponseEntity.ok(obterRestaurantePorIdUseCase.execute(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizarRestaurante(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        return ResponseEntity.ok(atualizarRestauranteUseCase.execute(id, restaurante));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Optional<Restaurante>> removerRestaurante(@PathVariable Long id) {
    	removerRestauranteUseCase.execute(id);
    	return ResponseEntity.noContent().build();
    }
}
