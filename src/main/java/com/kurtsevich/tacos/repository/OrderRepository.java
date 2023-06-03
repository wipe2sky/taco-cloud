package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.TacoOrder;
import com.kurtsevich.tacos.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface OrderRepository extends ReactiveCrudRepository<TacoOrder, String> {
    Flux<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
