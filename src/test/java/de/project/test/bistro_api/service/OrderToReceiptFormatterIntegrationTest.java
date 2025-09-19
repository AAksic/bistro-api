package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import de.project.test.bistro_api.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderToReceiptFormatterIntegrationTest {

    private final OrderToReceiptFormatter orderToReceiptFormatter = new OrderToReceiptFormatter();

    @Test
    void format_success() {
        Order order = Order.builder()
                .id(1)
                .build();
        order.setItems(
                List.of(
                        OrderItem.builder()
                                .id(1)
                                .product(Product.builder().id(1).name("Pizza").price(BigDecimal.valueOf(10.25)).build())
                                .order(order)
                                .quantity(5)
                                .build()
                ));

        String orderReceipt = orderToReceiptFormatter.format(order);

        assertThat(orderReceipt).contains("Table Nr. 1");
        assertThat(orderReceipt).contains("5 x Pizza @ 10,25 = 51,25");
        assertThat(orderReceipt).contains("Subtotal: 51,25");
    }

    @Test
    void format_success_multipleItems() {
        Order order = Order.builder()
                .id(1)
                .build();
        order.setItems(
                List.of(
                        OrderItem.builder()
                                .id(1)
                                .product(Product.builder().id(1).name("Pizza").price(BigDecimal.valueOf(10.25)).build())
                                .order(order)
                                .quantity(5)
                                .build(),
                        OrderItem.builder()
                                .id(2)
                                .product(Product.builder().id(2).name("Cola").price(BigDecimal.valueOf(2.5)).build())
                                .order(order)
                                .quantity(7)
                                .build()
                ));

        String orderReceipt = orderToReceiptFormatter.format(order);

        assertThat(orderReceipt).contains("Table Nr. 1");
        assertThat(orderReceipt).contains("5 x Pizza @ 10,25 = 51,25");
        assertThat(orderReceipt).contains("7 x Cola @ 2,50 = 17,50");
        assertThat(orderReceipt).contains("Subtotal: 68,75");
    }
}
