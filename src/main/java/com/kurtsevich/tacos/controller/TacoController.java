package com.kurtsevich.tacos.controller;

import com.kurtsevich.tacos.dto.TacoDto;
import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.mapper.TacoMapper;
import com.kurtsevich.tacos.repository.TacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/tacos",
        produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TacoController {
    private final TacoRepository tacoRepo;
    private final TacoMapper tacoMapper;

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") Long id) {
        return tacoRepo.findById(id);
    }

    @GetMapping(params = "recent")
    public Flux<TacoDto> getRecentTacos() {
        return tacoRepo.findAll().take(12)
                .map(tacoMapper::toDto);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> postTaco(@RequestBody Mono<Taco> tacoMono) {
        return tacoRepo.saveAll(tacoMono).next();
    }
}
