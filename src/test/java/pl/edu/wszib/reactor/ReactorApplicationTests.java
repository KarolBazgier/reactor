package pl.edu.wszib.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;


class ReactorApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void createFlux_just(){
		Flux<String> fruitFlux = Flux
				.just("jablko", "banan", "malina");

		StepVerifier.create(fruitFlux)
						.expectNext("jablko")
								.expectNext("banan")
										.expectNext("malina")
												.verifyComplete();
		
		fruitFlux.subscribe(System.out::println);
	}

	@Test
	public void createFlux_fromArray(){
		String[] fruits = new String[]{"jablko", "banan", "malina"};

		Flux<String> fruitFlux = Flux.fromArray(fruits);

		StepVerifier.create(fruitFlux)
				.expectNext("jablko")
				.expectNext("banan")
				.expectNext("malina")
				.verifyComplete();

	}


	@Test
	public void createFlux_fromIterable(){
		List<String> fruitList = new ArrayList<>();
		fruitList.add("jablko");
		fruitList.add("banan");
		fruitList.add("malina");

		Flux<String> fruitFlux = Flux.fromIterable(fruitList);

		StepVerifier.create(fruitFlux)
				.expectNext("jablko")
				.expectNext("banan")
				.expectNext("malina")
				.verifyComplete();

	}




}
