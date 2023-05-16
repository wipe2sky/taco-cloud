package com.kurtsevich.tacos.repository;

import com.kurtsevich.tacos.entity.TacoOrder;
import com.kurtsevich.tacos.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderRepository extends CrudRepository<TacoOrder, String> {
    List<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
