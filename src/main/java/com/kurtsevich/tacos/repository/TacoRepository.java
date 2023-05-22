package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
