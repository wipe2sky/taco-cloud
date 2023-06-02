package com.kurtsevich.tacos.controller;

import com.kurtsevich.tacos.entity.Ingredient;
import com.kurtsevich.tacos.entity.Ingredient.Type;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.entity.TacoOrder;
import com.kurtsevich.tacos.entity.User;
import com.kurtsevich.tacos.repository.IngredientRepository;
import com.kurtsevich.tacos.repository.TacoRepository;
import com.kurtsevich.tacos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;
    private final UserRepository userRepo;
    private final TacoRepository tacoRepo;

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                               filterByType(ingredients, type));
        }
    }


    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "user")
    public User user(Principal principal) {
        String username = principal.getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public RedirectView processTaco(@Valid Taco taco, Errors errors,
                                    @ModelAttribute("tacoOrder") TacoOrder tacoOrder, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return new RedirectView("design");
        }
        Taco saved = tacoRepo
                .save(taco)
                .blockOptional()
                .get();
        tacoOrder.addTaco(saved);

        attributes.addFlashAttribute("tacoOrder", tacoOrder);
        return new RedirectView("/orders/current");
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
