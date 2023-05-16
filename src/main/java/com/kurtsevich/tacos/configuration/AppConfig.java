package com.kurtsevich.tacos.configuration;

import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.entity.User;
import com.kurtsevich.tacos.repository.IngredientRepository;
import com.kurtsevich.tacos.repository.TacoRepository;
import com.kurtsevich.tacos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class AppConfig {
    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepo, UserRepository userRepo,
                                        TacoRepository tacoRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            Ingredient flourTortilla = ingredientRepo.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
            Ingredient cornTortilla = ingredientRepo.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
            Ingredient groundBeef = ingredientRepo.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
            Ingredient carnitas = ingredientRepo.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
            Ingredient tomatoes = ingredientRepo.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
            Ingredient lettuce = ingredientRepo.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
            Ingredient cheddar = ingredientRepo.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
            Ingredient jack = ingredientRepo.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
            Ingredient salsa = ingredientRepo.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
            Ingredient sourCream = ingredientRepo.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

            userRepo.save(new User(
                    "user",
                    passwordEncoder.encode("password"),
                    "Full Name",
                    "Street",
                    "City",
                    "State",
                    "222333",
                    "+375666666"
            ));

            Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(Arrays.asList(
                    flourTortilla, groundBeef, carnitas,
                    sourCream, salsa, cheddar));
            tacoRepo.save(taco1);

            Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(Arrays.asList(
                    cornTortilla, groundBeef, cheddar,
                    jack, sourCream));
            tacoRepo.save(taco2);

            Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(Arrays.asList(
                    flourTortilla, cornTortilla, tomatoes,
                    lettuce, salsa));
            tacoRepo.save(taco3);
        };
    }
}
