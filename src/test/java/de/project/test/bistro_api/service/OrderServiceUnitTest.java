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
        String expectedReceipt = MockDataFactory.generateMockReceipt();

        when(orderRepository.findById(eq(1L)))
                .thenReturn(Optional.of(mockOrder));
        when(discountService.apply(eq(mockOrder)))
                .thenReturn(mockOrder);
        when(orderFormatter.format(eq(mockOrder)))
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
        String expectedErrorMessage = "Order with id 1 does not exist";

        when(orderRepository.findById(1L))
                .thenThrow(new OrderNotFoundException(expectedErrorMessage));

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> unitUnderTest.getOrderById(1L));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(orderRepository, times(1)).findById(eq(1L));
        verify(discountService, never()).apply(eq(mockOrder));
        verify(orderFormatter, never()).format(eq(mockOrder));
    }
}