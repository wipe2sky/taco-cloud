package com.sia.tacos.controller;

import com.sia.tacos.entity.Taco;
import com.sia.tacos.repository.TacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/tacos",
        produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
@RequiredArgsConstructor
public class TacoController {
    private final TacoRepository tacoRepo;

    @GetMapping
    public Iterable<Taco> recentTacos(@RequestParam String recent) {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> getById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepo.findById(id);

        return optTaco.map(taco ->
                new ResponseEntity<>(taco, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco createTaco(@RequestBody Taco taco){
     return tacoRepo.save(taco);
    }
}
