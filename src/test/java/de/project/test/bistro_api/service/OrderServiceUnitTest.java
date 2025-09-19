package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.OrderNotFoundException;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.mapper.OrderRequestMapper;
import de.project.test.bistro_api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRequestMapper orderRequestMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderFormatter<String> orderFormatter;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private OrderServiceImpl unitUnderTest;

    @Test
    void placeOrder_success() {
        OrderRequest orderRequest = MockDataFactory.generateMockOrderRequest();
        Order mappedOrderFromRequest = MockDataFactory.generateMockOrderMappedFromRequest();
        Order savedOrder = MockDataFactory.generateMockSavedOrder();

        when(orderRequestMapper.toOrder(orderRequest))
                .thenReturn(mappedOrderFromRequest);
        when(orderRepository.save(mappedOrderFromRequest))
                .thenReturn(savedOrder);

        Order placedOrder = unitUnderTest.placeOrder(orderRequest);

        assertEquals(savedOrder.getId(), placedOrder.getId());
        assertEquals(savedOrder.getOrderDate(), placedOrder.getOrderDate());
        assertEquals(savedOrder.getItems().size(), placedOrder.getItems().size());
    }

    @Test
    void getOrderById_success() {
        Order mockOrder = MockDataFactory.generateMockOrder();
        String expectedReceipt = """
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

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(mockOrder));
        when(discountService.apply(mockOrder))
                .thenReturn(mockOrder);
        when(orderFormatter.format(mockOrder))
                .thenReturn(expectedReceipt);

        String orderReceipt = unitUnderTest.getOrderById(1L);

        assertEquals(expectedReceipt, orderReceipt);

        verify(orderRepository, times(1)).findById(eq(1L));
        verify(discountService, times(1)).apply(eq(mockOrder));
        verify(orderFormatter, times(1)).format(eq(mockOrder));
    }

    @Test
    void getOrderById_failure_orderDoesNotExist() {
        Order mockOrder = MockDataFactory.generateMockOrder();

        when(orderRepository.findById(1L))
                .thenThrow(OrderNotFoundException.class);

        assertThrows(OrderNotFoundException.class, () -> unitUnderTest.getOrderById(1L));

        verify(orderRepository, times(1)).findById(eq(1L));
        verify(discountService, never()).apply(eq(mockOrder));
        verify(orderFormatter, never()).format(eq(mockOrder));
    }
}