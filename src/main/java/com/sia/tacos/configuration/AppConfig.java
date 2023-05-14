package com.sia.tacos.configuration;

import com.sia.tacos.entity.Ingredient;
import com.sia.tacos.entity.Ingredient.Type;
import com.sia.tacos.entity.User;
import com.sia.tacos.repository.IngredientRepository;
import com.sia.tacos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(IngredientRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            repository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
            repository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
            repository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
            repository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
            repository.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
            repository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
            repository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
            repository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
            repository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
            repository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
            userRepository.save(new User(
                    "user",
                    passwordEncoder.encode("password"),
                    "Full Name",
                    "Street",
                    "City",
                    "State",
                    "222333",
                    "+375666666"
            ));
        };
    }
}
