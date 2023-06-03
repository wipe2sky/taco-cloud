package com.kurtsevich.tacos.controller;

import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/ingredients", consumes = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientRepository ingredientRepo;

    @GetMapping
    public Flux<Ingredient> getAll() {
        return ingredientRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Ingredient> create(@RequestBody Mono<Ingredient> ingredient) {
        return ingredient.flatMap(ingredientRepo::save);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        ingredientRepo.findBySlug(id)
                .doOnNext(ingredientRepo::delete)
                .subscribe();
    }
}
