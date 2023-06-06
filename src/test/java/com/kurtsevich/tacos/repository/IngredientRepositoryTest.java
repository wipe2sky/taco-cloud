package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Ingredient.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
class IngredientRepositoryTest {

    private Ingredient testIngredient1;
    private Ingredient testIngredient2;
    private Ingredient testIngredient3;
    @Autowired
    private IngredientRepository ingredientRepo;


    @BeforeEach
    void setup() {
        testIngredient1 = new Ingredient("FLTO", "Flour Tortilla", Type.WRAP);
        testIngredient2 = new Ingredient("GRBF", "Ground Beef", Type.PROTEIN);
        testIngredient3 = new Ingredient("CHED", "Cheddar Cheese", Type.CHEESE);

        Flux<Ingredient> deleteAndInsert = ingredientRepo.deleteAll()
                .thenMany(ingredientRepo.saveAll(
                        Flux.just(
                                testIngredient1,
                                testIngredient2,
                                testIngredient3
                                 )));
        StepVerifier.create(deleteAndInsert)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void shouldSaveAndFetchIngredients() {
        StepVerifier.create(ingredientRepo.findAll())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .consumeRecordedWith(ingredients -> {
                    assertThat(ingredients).hasSize(3);
                    assertThat(ingredients).contains(testIngredient1);
                    assertThat(ingredients).contains(testIngredient2);
                    assertThat(ingredients).contains(testIngredient3);
                })
                .verifyComplete();

    }

    @Test
    void shouldFindIngredientBySlug() {
        StepVerifier.create(ingredientRepo.findBySlug("FLTO"))
                .assertNext(ingredient ->
                                    assertThat(ingredient).isEqualTo(
                                            testIngredient1))
                .verifyComplete();
    }

    @Test
    void shouldDeleteIngredientSlug() {

        StepVerifier.create(ingredientRepo.findBySlug(testIngredient1.getSlug()))
                .assertNext(ingredient ->
                                    assertThat(ingredient).isEqualTo(
                                            testIngredient1))
                .verifyComplete();

        StepVerifier.create(ingredientRepo.delete(testIngredient1))
                .verifyComplete();

        StepVerifier.create(ingredientRepo.findBySlug(testIngredient1.getSlug()))
                .expectNextCount(0)
                .verifyComplete();
    }
}
