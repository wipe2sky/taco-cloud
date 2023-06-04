package com.kurtsevich.tacos.dto;

import com.kurtsevich.tacos.entity.Ingredient;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TacoDto {
    private Long id;

    @NotBlank
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();

    public TacoDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
