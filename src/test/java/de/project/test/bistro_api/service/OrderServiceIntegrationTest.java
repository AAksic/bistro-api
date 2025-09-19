package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.OrderNotFoundException;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.mapper.OrderRequestMapper;
import de.project.test.bistro_api.repository.OrderRepository;
import de.project.test.bistro_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderServiceIntegrationTest {

    @Autowired
    private OrderRequestMapper orderRequestMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFormatter<String> orderFormatter;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private OrderServiceImpl unitUnderTest;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.saveAll(MockDataFactory.generateMockProducts());
    }

    @Test
    void placeOrder_success() {
        OrderRequest orderRequest = OrderRequest.builder()
                .orderItems(List.of(
                                OrderItemRequest.builder().productId(1).quantity(2).build()
                        )
                )
                .build();

        Order placedOrder = unitUnderTest.placeOrder(orderRequest);

        assertEquals(1L, placedOrder.getId());
        assertThat(placedOrder.getItems()).hasSize(1);

        OrderItem first = placedOrder.getItems().getFirst();
        assertEquals(1, first.getProduct().getId());
        assertEquals(2, first.getQuantity());
        assertEquals("Pizza", first.getProduct().getName());
        assertEquals(BigDecimal.valueOf(10.25), first.getProduct().getPrice());
        assertEquals(50, first.getProduct().getStock());
    }

    @Test
    void getOrderById_success() {
        Order orderToSave = Order.builder().build();
        orderToSave.setItems(
                List.of(
                        OrderItem.builder()
                                .product(Product.builder().id(1).build())
                                .order(orderToSave)
                                .quantity(5)
                                .build()
                ));
        Order savedOrder = orderRepository.save(orderToSave);

        String retrievedOrder = unitUnderTest.getOrderById(savedOrder.getId());

        assertThat(retrievedOrder).contains("Table Nr. 1");
        assertThat(retrievedOrder).contains("5 x Pizza @ 10,25 = 51,25");
        assertThat(retrievedOrder).contains("Subtotal: 51,25");
    }

    @Test
    void getOrderById_failure_orderDoesNotExist() {
        String expectedErrorMessage = "Order with id 5 does not exist";

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> unitUnderTest.getOrderById(5L));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

}