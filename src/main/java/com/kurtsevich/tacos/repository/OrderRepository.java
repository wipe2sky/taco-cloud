package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.TacoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface OrderRepository extends ReactiveCrudRepository<TacoOrder, String> {
}
