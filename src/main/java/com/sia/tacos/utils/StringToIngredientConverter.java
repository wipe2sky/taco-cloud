package com.sia.tacos.utils;

import com.sia.tacos.entity.Ingredient;
import com.sia.tacos.entity.IngredientUDT;
import com.sia.tacos.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StringToIngredientConverter implements Converter<String, IngredientUDT> {

    private final IngredientRepository ingredientRepository;

    @Override
    public IngredientUDT convert(String id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        return ingredient.isPresent()
                ? ingredient.map(i ->
                new IngredientUDT(i.getName(), i.getType())
        ).get()
                : null;
    }

}
