package com.kurtsevich.tacos;

import com.kurtsevich.tacos.controller.TacoController;
import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.repository.TacoRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TacoControllerTest {
    @Test
    @SneakyThrows
    void shouldReturnRecentTacos() {
        Taco[] tacos = {
                testTaco(1L), testTaco(2L),
                testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L),
                testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L),
                testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L),
                testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
        when(tacoRepo.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoRepo))
                .build();

        ClassPathResource recentResource =
                new ClassPathResource("recent-tacos.json");
        String recentJson = StreamUtils.copyToString(recentResource.getInputStream(), Charset.defaultCharset());

        //With a parsing of the web test client response
        testClient.get().uri("/api/tacos?recent").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                .jsonPath("$[0].name").isEqualTo("Taco 1")
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                .jsonPath("$[1].name").isEqualTo("Taco 2")
                .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
                .jsonPath("$[11].name").isEqualTo("Taco 12")
                .jsonPath("$[12]").doesNotExist();

        //Using prepared json data for comparison
        testClient.get().uri("/api/tacos?recent").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody().json(recentJson);

        //With a list of values
        testClient.get().uri("/api/tacos?recent").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().expectBodyList(Taco.class)
                .contains(Arrays.copyOf(tacos, 12));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldSaveATaco() {
        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
        WebTestClient testClient = WebTestClient
                .bindToController(new TacoController((tacoRepo)))
                .build();
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(1L));
        Taco savedTaco = testTaco(1L);
        Flux<Taco> savedTacoMono = Flux.just(savedTaco);

        when(tacoRepo.saveAll(any(Mono.class))).thenReturn(savedTacoMono);

        testClient.post()
                .uri("/api/tacos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        taco.setId(number != null ? number : 11111);
        taco.setName("Taco " + number);
        taco.setCreatedAt(Date.valueOf(LocalDate.of(2023, 06, 02)));
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(
                new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(
                new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredients(ingredients);
        return taco;
    }
}
