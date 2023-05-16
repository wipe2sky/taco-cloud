package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.Taco;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TacoRepository extends PagingAndSortingRepository<Taco, Long> {
}
