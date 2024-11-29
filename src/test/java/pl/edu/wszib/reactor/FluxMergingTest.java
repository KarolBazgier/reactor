package pl.edu.wszib.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxMergingTest {
    @Test
    public void mergeFluxes(){
        Flux<String> fruitFlux = Flux
                .just("jablko", "banan", "malina")
                .delayElements(Duration.ofMillis(500));

        Flux<String> fruitColorsFlux = Flux
                .just("czerwony", "zolty", "rozowy")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

//        Flux<String> mergedFlux = fruitFlux.mergeWith(fruitColorsFlux);

        Flux<String> mergedFlux = Flux.zip(fruitFlux, fruitColorsFlux, (fruit, color) -> fruit + " - " + color);

        mergedFlux
                .doOnNext(item -> System.out.println("Emitting: " + item))
                .subscribe() ; // Wypisanie każdego elementu
//        StepVerifier.create(mergedFlux)
//                .expectNext("jablko")
//                .expectNext("czerwony")
//                .expectNext("banan")
//                .expectNext("zolty")
//                .expectNext("malina")
//                .expectNext("rozowy")
//                .verifyComplete();
        StepVerifier.create(mergedFlux)
                .expectNext("jablko - czerwony")
                .expectNext("banan - zolty")
                .expectNext("malina - rozowy")
                .verifyComplete();

    }

    @Test
    public void firstFlux(){
        Flux<String> slowFlux = Flux.just("zolw", "slimak")
                .delaySubscription(Duration.ofMillis(500));

        Flux<String> fastFlux = Flux.just("puma", "strus");

        Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(fastFlux)
                .expectNext("puma")
                .expectNext("strus")
                .verifyComplete();
    }
}
