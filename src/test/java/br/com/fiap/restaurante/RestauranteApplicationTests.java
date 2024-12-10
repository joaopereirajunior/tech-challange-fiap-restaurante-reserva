package br.com.fiap.restaurante;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class RestauranteApplicationTests {

	@Test
	void contextLoads() {
	}

}
