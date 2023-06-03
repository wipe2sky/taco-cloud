package com.kurtsevich.tacos;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class TacoControllerWebTest {
    private final WebTestClient testClient;

    @Test
    void shouldReturnRecentTacos() {
        testClient.get().uri("/api/tacos/recent")
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[?(@.name == 'Carnivore')]").exists()
                .jsonPath("$[?(@.name == 'Bovine Bounty')]").exists()
                .jsonPath("$[?(@.name == 'Veg-Out')]").exists();

    }
}
