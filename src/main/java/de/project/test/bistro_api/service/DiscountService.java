package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.function.Predicate;

@Service
public class DiscountService {

    private static final Predicate<LocalTime> IS_HAPPY_HOUR =
            currentTime -> currentTime.isAfter(LocalTime.of(3, 0)) && currentTime.isBefore(LocalTime.of(4, 0));
    private static final Predicate<Long> IS_666_TH_ORDER =
            orderId -> orderId % 666 == 0;


    public Order apply(Order eligibleOrder) {
        LocalTime orderTime = LocalDateTime.ofInstant(eligibleOrder.getOrderDate(), ZoneOffset.UTC).toLocalTime();

        if (IS_HAPPY_HOUR.test(orderTime)) {
            eligibleOrder.setDiscount(20);
        }

        if (IS_666_TH_ORDER.test(eligibleOrder.getId())) {
            eligibleOrder.setDiscount(100);
        }

        return eligibleOrder;
    }
}
