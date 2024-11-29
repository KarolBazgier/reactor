package pl.edu.wszib.reactor;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class FluxTransformigTest {
    @Test
    public void skipAFew() {
        Flux<String> skipFlux = Flux.just("1", "2", "3", "4", "5")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("4", "5")
                .verifyComplete();
    }

    @Test
    public void skipAFewSeconds() {
        Flux<String> skipFlux = Flux.just("1", "2", "3", "4", "5")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        StepVerifier.create(skipFlux)
                .expectNext("4", "5")
                .verifyComplete();
    }

    @Test
    public void take() {
        Flux<String> takeFlux = Flux.just("1", "2", "3", "4", "5")
                .take(2);

        StepVerifier.create(takeFlux)
                .expectNext("1", "2")
                .verifyComplete();
    }

    @Test
    public void takeForTime() {
        Flux<String> takeFlux = Flux.just("1", "2", "3", "4", "5")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofSeconds(2));

        StepVerifier.create(takeFlux)
                .expectNext("1", "2")
                .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> names = Flux.just("Karol Piotr", "Adam", "Piotr Pawel", "Darek")
                .filter(name -> !name.contains(" "));

        StepVerifier.create(names)
                .expectNext("Adam", "Darek")
                .verifyComplete();

    }

    @Test
    public void distinct() {
        Flux<String> names = Flux.just("Karol", "Adam", "Karol", "Darek")
                .distinct();

        StepVerifier.create(names)
                .expectNext("Karol", "Adam", "Darek")
                .verifyComplete();

    }

    @Test
    public void map() {
        Flux<Player> playerFlux = Flux.just("Cristiano Ronaldo", "Leo Messi", "Robert Lewandowski")
                .map(player -> {
                    String[] split = player.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Cristiano", "Ronaldo"))
                .expectNext(new Player("Leo", "Messi"))
                .expectNext(new Player("Robert", "Lewandowski"))
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux.just("Cristiano Ronaldo", "Leo Messi", "Robert Lewandowski")
                .flatMap(n -> Mono.just(n)
                        .map(player -> {
                            String[] split = player.split("\\s");
                            return new Player(split[0], split[1]);
                        }).subscribeOn(Schedulers.parallel())
                );

        List<Player> players = Arrays.asList(
                new Player("Robert", "Lewandowski"),
                new Player("Cristiano", "Ronaldo"),
                new Player("Leo", "Messi")
        );

        StepVerifier.create(playerFlux)
                .expectNextMatches(player -> players.contains(player))
                .expectNextMatches(player -> players.contains(player))
                .expectNextMatches(player -> players.contains(player))
                .verifyComplete();
    }


    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;
    }

}
