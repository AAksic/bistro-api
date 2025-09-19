package de.project.test.bistro_api.factory;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.dto.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class MockDataFactory {

    public static Product generateMockProduct() {
        return Product.builder()
                .id(1L)
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
                        .id(1L)
                        .name("Pizza")
                        .price(BigDecimal.valueOf(10.25))
                        .stock(50)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("Cola")
                        .price(BigDecimal.valueOf(2.5))
                        .stock(200)
                        .build(),
                Product.builder()
                        .id(3L)
                        .name("Water")
                        .price(BigDecimal.valueOf(1.5))
                        .stock(1000)
                        .build(),
                Product.builder()
                        .id(4L)
                        .name("Chicken Tikka Masala")
                        .price(BigDecimal.valueOf(12.75))
                        .stock(15)
                        .build()
        );
    }

    public static Order generateMockOrder() {
        return Order.builder()
                .id(1L)
                .orderDate(Instant.parse("2025-09-18T18:00:00.00Z"))
                .build();
    }

    public static OrderRequest generateMockOrderRequest() {
        return OrderRequest.builder()
                .orderItems(List.of(
                        OrderItemRequest.builder()
                                .productId(1L)
                                .quantity(2)
                                .build()
                ))
                .build();
    }

    public static Order generateMockOrderMappedFromRequest() {
        return Order.builder()
                .items(List.of(
                        OrderItem.builder()
                                .quantity(2)
                                .product(Product.builder()
                                        .id(1L)
                                        .build())
                                .build()
                ))
                .build();
    }

    public static Order generateMockSavedOrder() {
        return Order.builder()
                .id(1L)
                .items(List.of(
                        OrderItem.builder()
                                .id(1L)
                                .quantity(2)
                                .product(Product.builder()
                                        .id(1)
                                        .name("Pizza")
                                        .price(BigDecimal.valueOf(12.50))
                                        .stock(50)
                                        .build())
                                .build()
                ))
                .orderDate(Instant.parse("2025-09-18T18:00:00.00Z"))
                .build();
    }

    public static String generateMockReceipt() {
        return """
                -------------------------
                Table Nr. 10
                -------------------------
                2 x Pizza @ 10.0 = 20.0
                2 x Cola @ 2.5 = 5.0
                -------------------------
                Subtotal: 25.0
                Discount: 0%
                Total: 25.0
                """;
    }
}
