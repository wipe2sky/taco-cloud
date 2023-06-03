package com.kurtsevich.tacos.utils;

import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<Long, Ingredient> {
    private final IngredientRepository ingredientRepository;

    @Override
    public Ingredient convert(Long id) {
        return ingredientRepository.findById(id).blockOptional().orElse(null);
    }
}
