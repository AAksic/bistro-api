package de.project.test.bistro_api.service;

import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductStockUpdaterServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockUpdaterService unitUnderTest;

    @BeforeEach
    void setup() {
        productRepository.saveAll(MockDataFactory.generateMockProducts());
    }

    @AfterEach
    void teardown() {
        productRepository.deleteAll();
    }

    @Test
    void updateStock_success() {
        OrderRequest orderRequest = OrderRequest.builder()
                .orderItems(List.of(
                        OrderItemRequest.builder()
                                .productId(1L)
                                .quantity(25)
                                .build()
                ))
                .build();

        unitUnderTest.updateStock(orderRequest);

        assertEquals(25, productRepository.findById(1L).get().getStock());
    }
}
