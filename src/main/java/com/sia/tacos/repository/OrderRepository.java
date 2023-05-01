package com.sia.tacos.repository;

import com.sia.tacos.entity.TacoOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
