package com.sia.tacos.repository;

import com.sia.tacos.entity.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<TacoOrder, String> {
}
