package com.sia.tacos.controller;

import com.sia.tacos.entity.Taco;
import com.sia.tacos.repository.TacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/tacos",
        produces = "application/json")
@CrossOrigin(origins="http://tacocloud:8080")
@RequiredArgsConstructor
public class TacoController {
    private final TacoRepository tacoRepo;

    @GetMapping(params = "recent")
    public Iterable<Taco> recentTacos(){
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }
}
