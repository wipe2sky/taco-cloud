package com.kurtsevich.tacos.mapper;

import com.kurtsevich.tacos.dto.TacoDto;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TacoMapper {
    private final IngredientRepository ingredientRepo;

    public TacoDto toDto(Taco taco) {
        TacoDto tacoDto =
                new TacoDto(taco.getId(), taco.getName());

        taco.getIngredientIds()
                .forEach(ingredientId ->
                                 ingredientRepo.findById(ingredientId)
                                         .subscribe(tacoDto::addIngredient));
        return tacoDto;
    }
}
