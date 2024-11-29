package pl.edu.wszib.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BufferingFluxTest {
    @Test
    public void buffer(){
        Flux<String> fruitFlux = Flux.just("jablko", "banan", "malina", "kiwi", "granat");

        Flux<List<String>> bufferedFruitFLux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFruitFLux)
                .expectNext(Arrays.asList("jablko", "banan", "malina"))
                .expectNext(Arrays.asList("kiwi", "granat"))
                .verifyComplete();
    }

    @Test
    public void bufferAndFlatMap(){
        Flux.just("jablko", "banan", "malina", "kiwi", "granat")
                .buffer(3)
                .flatMap(x->
                        Flux.fromIterable(x)
                                .map(y-> y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();

    }

    @Test
    public void collectToList(){
        Flux<String> fruitFlux = Flux.just("jablko", "banan", "malina", "kiwi", "granat");

        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(Arrays.asList("jablko", "banan", "malina", "kiwi", "granat"))
                .verifyComplete();
    }

    @Test
    public void collectToMap(){
        Flux<String> fruitFlux = Flux.just("jablko", "banan", "malina", "kiwi", "granat");

        Mono<Map<Character, String>> fruitMapMono = fruitFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(fruitMapMono)
                .expectNextMatches(map -> {
                    return map.size() == 5 &&
                            map.get('j').equals("jablko") &&
                            map.get('b').equals("banan") &&
                            map.get('m').equals("malina") &&
                            map.get('k').equals("kiwi") &&
                            map.get('g').equals("granat");
                }).verifyComplete();
    }

    @Test
    public void all(){
        Flux<String> fruitFlux = Flux.just("jablko", "banan", "malina", "granat");

        Mono<Boolean> hasAMono = fruitFlux.all(a -> a.contains("a"));
        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasJMono = fruitFlux.all(a -> a.contains("j"));
        StepVerifier.create(hasJMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void any(){
        Flux<String> fruitFlux = Flux.just("jablko", "banan", "malina", "granat");

        Mono<Boolean> hasJMono = fruitFlux.any(a -> a.contains("j"));
        StepVerifier.create(hasJMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasZMono = fruitFlux.any(a -> a.contains("z"));
        StepVerifier.create(hasZMono)
                .expectNext(false)
                .verifyComplete();
    }
}
