package de.project.test.bistro_api.factory;

import de.project.test.bistro_api.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class MockDataFactory {

    public static Product generateMockProduct() {
        return Product.builder()
                .id(1)
                .name("Wiener Schnitzel")
                .price(BigDecimal.valueOf(17.50))
                .quantity(35)
                .build();
    }

    public static List<Product> generateMockProducts() {
        return List.of(
                Product.builder()
                        .name("Pizza")
                        .price(BigDecimal.valueOf(10.25))
                        .quantity(50)
                        .build(),
                Product.builder()
                        .name("Cola")
                        .price(BigDecimal.valueOf(2.50))
                        .quantity(200)
                        .build(),
                Product.builder()
                        .name("Wasser")
                        .price(BigDecimal.valueOf(1.5))
                        .quantity(1000)
                        .build(),
                Product.builder()
                        .name("Chicken Tikka Masala")
                        .price(BigDecimal.valueOf(12.75))
                        .quantity(15)
                        .build()
        );
    }
}
