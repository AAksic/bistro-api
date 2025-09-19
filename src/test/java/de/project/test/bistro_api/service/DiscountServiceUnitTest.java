package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DiscountServiceUnitTest {

    @Spy
    private Order order;

    private DiscountService unitUnderTest;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        unitUnderTest = new DiscountService();
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() throws Exception {
        closeable.close();
    }

    @Test
    void apply_isHappyHour() {
        doReturn(Instant.parse("2025-09-18T18:00:00.00Z"))
                .when(order).getOrderDate();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(20, appliedOrder.getDiscount());

        verify(order, times(1)).setDiscount(anyInt());
    }

    @Test
    void apply_is42ndOrder() {
        doReturn(Instant.parse("2025-09-18T14:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(42L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(50, appliedOrder.getDiscount());

        verify(order, times(1)).setDiscount(anyInt());
    }

    @Test
    void apply_is666ndOrder() {
        doReturn(Instant.parse("2025-09-18T14:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(666L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(100, appliedOrder.getDiscount());

        verify(order, times(1)).setDiscount(anyInt());
    }

    @Test
    void apply_isHappyHourAnd42ndOrder() {
        doReturn(Instant.parse("2025-09-18T18:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(42L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(50, appliedOrder.getDiscount());

        verify(order, times(2)).setDiscount(anyInt());
    }

    @Test
    void apply_isHappyHourAnd666thOrder() {
        doReturn(Instant.parse("2025-09-18T18:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(666L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(100, appliedOrder.getDiscount());

        verify(order, times(2)).setDiscount(anyInt());
    }

    @Test
    void apply_is42ndOrderAnd666thOrder() {
        doReturn(Instant.parse("2025-09-18T14:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(4662L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(100, appliedOrder.getDiscount());

        verify(order, times(2)).setDiscount(anyInt());
    }

    @Test
    void apply_isHappyHourAnd42ndOrderAnd666thOrder() {
        doReturn(Instant.parse("2025-09-18T18:00:00.00Z"))
                .when(order).getOrderDate();
        doReturn(4662L)
                .when(order).getId();

        Order appliedOrder = unitUnderTest.apply(order);

        assertEquals(100, appliedOrder.getDiscount());

        verify(order, times(3)).setDiscount(anyInt());
    }
}
