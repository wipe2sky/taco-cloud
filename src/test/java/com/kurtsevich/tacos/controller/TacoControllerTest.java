package com.kurtsevich.tacos.controller;

import com.kurtsevich.tacos.dto.TacoDto;
import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.mapper.TacoMapper;
import com.kurtsevich.tacos.repository.TacoRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        TacoMapper tacoMapper = createTacoMapperMock();

        WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoRepo, tacoMapper))
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
                .expectBody()
                .json(recentJson);

        //With a list of values
        List<Mono<TacoDto>> monoList = Arrays.stream(tacos)
                .limit(12)
                .map(tacoMapper::toDto)
                .collect(Collectors.toList());

        Flux<TacoDto> fluxOfTacoDto = Flux.fromIterable(monoList)
                .flatMap(mono -> mono)
                .collectList()
                .flatMapMany(Flux::fromIterable);

        TacoDto[] tacosDto = fluxOfTacoDto
                .collectList()
                .block()
                .toArray(TacoDto[]::new);

        testClient.get().uri("/api/tacos?recent").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBodyList(TacoDto.class)
                .contains(tacosDto);
    }


    @SuppressWarnings("unchecked")
    @Test
    void shouldSaveATaco() {
        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);

        TacoMapper tacoMapper = createTacoMapperMock();

        WebTestClient testClient = WebTestClient
                .bindToController(new TacoController(tacoRepo, tacoMapper))
                .build();

        Mono<TacoDto> unsavedTacoDtoMono = tacoMapper.toDto(testTaco(1L));

        Taco savedTaco = testTaco(1L);
        Flux<Taco> savedTacoMono = Flux.just(savedTaco);

        TacoDto savedTacoDto = tacoMapper.toDto(savedTaco).block();

        when(tacoRepo.saveAll(any(Mono.class))).thenReturn(savedTacoMono);

        testClient.post()
                .uri("/api/tacos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoDtoMono, TacoDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TacoDto.class)
                .isEqualTo(savedTacoDto);
    }

    private TacoMapper createTacoMapperMock() {
        TacoMapper tacoMapper = Mockito.mock(TacoMapper.class);
        when(tacoMapper.toDto(any(Taco.class))).thenAnswer((Answer<Mono<TacoDto>>) invocation -> {
            Taco taco = invocation.getArgument(0);

            TacoDto tacoDto = new TacoDto(taco.getId(), taco.getName());
            Ingredient ingredient1 = new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP);
            Ingredient ingredient2 = new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN);
            ingredient1.setId(1L);
            ingredient2.setId(2L);
            tacoDto.addIngredient(ingredient1);
            tacoDto.addIngredient(ingredient2);
            return Mono.just(tacoDto);
        });


        when(tacoMapper.toEntity(any(TacoDto.class)))
                .thenAnswer((Answer<Mono<Taco>>) invocation -> {
                    TacoDto tacoDto = invocation.getArgument(0);
                    Taco taco = new Taco();
                    taco.setName(tacoDto.getName());
                    tacoDto.getIngredients().forEach(taco::addIngredient);
                    return Mono.just(taco);
                });

        return tacoMapper;
    }

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        taco.setId(number != null ? number : 11111);
        taco.setName("Taco " + number);
        Ingredient ingredient1 = new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP);
        Ingredient ingredient2 = new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN);
        ingredient1.setId(1L);
        ingredient2.setId(2L);
        taco.addIngredient(ingredient1);
        taco.addIngredient(ingredient2);
        return taco;
    }


}
