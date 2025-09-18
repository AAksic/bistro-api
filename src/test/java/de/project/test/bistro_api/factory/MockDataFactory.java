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
                .stock(35)
                .build();
    }

    public static List<Product> generateMockProducts() {
        return List.of(
                Product.builder()
                        .name("Pizza")
                        .price(BigDecimal.valueOf(10.25))
                        .stock(50)
                        .build(),
                Product.builder()
                        .name("Cola")
                        .price(BigDecimal.valueOf(2.5))
                        .stock(200)
                        .build(),
                Product.builder()
                        .name("Water")
                        .price(BigDecimal.valueOf(1.5))
                        .stock(1000)
                        .build(),
                Product.builder()
                        .name("Chicken Tikka Masala")
                        .price(BigDecimal.valueOf(12.75))
                        .stock(15)
                        .build()
        );
    }

    public static List<Product> generateMockProductsWithIds() {
        return List.of(
                Product.builder()
                        .id(1)
                        .name("Pizza")
                        .price(BigDecimal.valueOf(10.25))
                        .stock(50)
                        .build(),
                Product.builder()
                        .id(2)
                        .name("Cola")
                        .price(BigDecimal.valueOf(2.5))
                        .stock(200)
                        .build(),
                Product.builder()
                        .id(3)
                        .name("Water")
                        .price(BigDecimal.valueOf(1.5))
                        .stock(1000)
                        .build(),
                Product.builder()
                        .id(4)
                        .name("Chicken Tikka Masala")
                        .price(BigDecimal.valueOf(12.75))
                        .stock(15)
                        .build()
        );
    }
}
