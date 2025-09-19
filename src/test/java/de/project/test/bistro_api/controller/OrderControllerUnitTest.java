package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.OrderNotFoundException;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.service.OrderService;
import de.project.test.bistro_api.service.ProductStockUpdaterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductStockUpdaterService productStockUpdaterService;

    @InjectMocks
    private OrderController unitUnderTest;

    @Test
    void placeOrder_success() {
        OrderRequest mockOrderRequest = MockDataFactory.generateMockOrderRequest();
        Order mockPlacedOrder = MockDataFactory.generateMockSavedOrder();

        when(orderService.placeOrder(eq(mockOrderRequest)))
                .thenReturn(mockPlacedOrder);
        doNothing()
                .when(productStockUpdaterService).updateStock(eq(mockOrderRequest));

        Order placedOrder = unitUnderTest.placeOrder(mockOrderRequest);

        assertEquals(mockPlacedOrder, placedOrder);

        verify(productStockUpdaterService, times(1)).updateStock(mockOrderRequest);
    }

    @Test
    void getOrderById_success() {
        String mockReceipt = MockDataFactory.generateMockReceipt();

        when(orderService.getOrderById(eq(1L)))
                .thenReturn(mockReceipt);

        String receipt = orderService.getOrderById(1L);

        assertEquals(mockReceipt, receipt);
    }

    @Test
    void getOrderById_failure_orderDoesNotExist() {
        String expectedErrorMessage = "Order with id 1 does not exist";

        when(orderService.getOrderById(eq(1L)))
                .thenThrow(new OrderNotFoundException(expectedErrorMessage));

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> unitUnderTest.getOrderById(1L));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}