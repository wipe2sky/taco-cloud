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

import static com.kurtsevich.tacos.entity.Ingredient.Type;
import static com.kurtsevich.tacos.entity.Ingredient.Type.PROTEIN;
import static com.kurtsevich.tacos.entity.Ingredient.Type.WRAP;

@Profile("dev")
@Configuration
public class DevelopmentConfig {
    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo, UserRepository userRepo,
                                        PasswordEncoder encoder, TacoRepository tacoRepo) {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Ingredient flourTortilla = saveAnIngredient("FLTO", "Flour Tortilla", WRAP);
                Ingredient cornTortilla = saveAnIngredient("COTO", "Corn Tortilla", WRAP);
                Ingredient groundBeef = saveAnIngredient("GRBF", "Ground Beef", PROTEIN);
                Ingredient carnitas = saveAnIngredient("CARN", "Carnitas", Type.PROTEIN);
                Ingredient tomatoes = saveAnIngredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
                Ingredient lettuce = saveAnIngredient("LETC", "Lettuce", Type.VEGGIES);
                Ingredient cheddar = saveAnIngredient("CHED", "Cheddar", Type.CHEESE);
                Ingredient jack = saveAnIngredient("JACK", "Monterrey Jack", Type.CHEESE);
                Ingredient salsa = saveAnIngredient("SLSA", "Salsa", Type.SAUCE);
                Ingredient sourCream = saveAnIngredient("SRCR", "Sour Cream", Type.SAUCE);
                Taco taco1 = new Taco();
                taco1.setName("Carnivore");
                taco1.addIngredient(flourTortilla);
                taco1.addIngredient(groundBeef);
                taco1.addIngredient(carnitas);
                taco1.addIngredient(sourCream);
                taco1.addIngredient(salsa);
                taco1.addIngredient(cheddar);
                tacoRepo.save(taco1).subscribe();

                Taco taco2 = new Taco();
                taco2.setName("Bovine Bounty");
                taco2.addIngredient(cornTortilla);
                taco2.addIngredient(groundBeef);
                taco2.addIngredient(cheddar);
                taco2.addIngredient(jack);
                taco2.addIngredient(sourCream);
                tacoRepo.save(taco2).subscribe();

                Taco taco3 = new Taco();
                taco3.setName("Veg-Out");
                taco3.addIngredient(flourTortilla);
                taco3.addIngredient(cornTortilla);
                taco3.addIngredient(tomatoes);
                taco3.addIngredient(lettuce);
                taco3.addIngredient(salsa);
                tacoRepo.save(taco3).subscribe();

                userRepo.save(new User(
                        "user",
                        encoder.encode("password"),
                        "Full Name",
                        "Street",
                        "City",
                        "State",
                        "222333",
                        "+375666666"
                )).subscribe();
            }

            private Ingredient saveAnIngredient(String id, String name, Type type) {
                Ingredient ingredient = new Ingredient(id, name, type);
                repo.save(ingredient).subscribe();
                return ingredient;
            }
        };
    }
}
