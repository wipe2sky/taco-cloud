package com.kurtsevich.tacos.mapper;

import com.kurtsevich.tacos.dto.TacoDto;
import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TacoMapper {
    private final IngredientRepository ingredientRepo;

    public Mono<TacoDto> toDto(Taco taco) {
        Flux<Ingredient> ingredientFlux = ingredientRepo.findAllById(taco.getIngredientIds());
        return ingredientFlux.collectList()
                .map(ingredients -> {
                    TacoDto tacoDto = new TacoDto(taco.getId(), taco.getName());
                    tacoDto.setIngredients(ingredients);
                    return tacoDto;
                });
    }

    public Mono<Taco> toEntity(TacoDto tacoDto) {
            Taco taco = new Taco();
            taco.setName(tacoDto.getName());
            tacoDto.getIngredients().forEach(taco::addIngredient);
            return Mono.just(taco);
    }
}
