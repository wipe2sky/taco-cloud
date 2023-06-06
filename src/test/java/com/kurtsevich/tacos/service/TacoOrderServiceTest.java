package com.kurtsevich.tacos.service;

import com.kurtsevich.tacos.entity.Taco;
import com.kurtsevich.tacos.entity.TacoOrder;
import com.kurtsevich.tacos.repository.OrderRepository;
import com.kurtsevich.tacos.repository.TacoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataR2dbcTest
@DirtiesContext
class TacoOrderServiceTest {
    @Autowired
    TacoRepository tacoRepo;
    @Autowired
    OrderRepository orderRepo;

    TacoOrderService service;
    TacoOrder newOrder;

    @BeforeEach
    void setup() {
        this.service = new TacoOrderService(tacoRepo, orderRepo);

        newOrder = new TacoOrder();
        newOrder.setDeliveryName("Test Customer");
        newOrder.setDeliveryStreet("1234 North Street");
        newOrder.setDeliveryCity("Notrees");
        newOrder.setDeliveryState("TX");
        newOrder.setDeliveryZip("79759");
        newOrder.setCcNumber("4111111111111111");
        newOrder.setCcExpiration("12/24");
        newOrder.setCcCVV("123");
        newOrder.addTaco(new Taco("Test Taco One"));
        newOrder.addTaco(new Taco("Test Taco Two"));

        Flux<Taco> deleteAndInsertTaco = tacoRepo.deleteAll()
                .thenMany(tacoRepo.saveAll(newOrder.getTacos()));

        StepVerifier.create(deleteAndInsertTaco)
                .expectNextCount(2)
                .verifyComplete();

        Flux<TacoOrder> deleteAndInsertOrder = orderRepo.deleteAll()
                .thenMany(service.save(newOrder));

        StepVerifier.create(deleteAndInsertOrder)
                .assertNext(this::assertOrder)
                .verifyComplete();

        StepVerifier.create(orderRepo.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldFindOrderById() {
        StepVerifier.create(service.findById(1L))
                .assertNext(this::assertOrder)
                .verifyComplete();
    }

    private void assertOrder(TacoOrder savedOrder) {
        assertThat(savedOrder.getId()).isEqualTo(1L);
        assertThat(savedOrder.getDeliveryName()).isEqualTo("Test Customer");
        assertThat(savedOrder.getDeliveryName()).isEqualTo("Test Customer");
        assertThat(savedOrder.getDeliveryStreet()).isEqualTo("1234 North Street");
        assertThat(savedOrder.getDeliveryCity()).isEqualTo("Notrees");
        assertThat(savedOrder.getDeliveryState()).isEqualTo("TX");
        assertThat(savedOrder.getDeliveryZip()).isEqualTo("79759");
        assertThat(savedOrder.getCcNumber()).isEqualTo("4111111111111111");
        assertThat(savedOrder.getCcExpiration()).isEqualTo("12/24");
        assertThat(savedOrder.getCcCVV()).isEqualTo("123");
        assertThat(savedOrder.getTacoIds()).hasSize(2);
        assertThat(savedOrder.getTacos().get(0).getId()).isEqualTo(1L);
        assertThat(savedOrder.getTacos().get(0).getName()).isEqualTo("Test Taco One");
        assertThat(savedOrder.getTacos().get(1).getId()).isEqualTo(2L);
        assertThat(savedOrder.getTacos().get(1).getName())
                .isEqualTo("Test Taco Two");
    }
}
