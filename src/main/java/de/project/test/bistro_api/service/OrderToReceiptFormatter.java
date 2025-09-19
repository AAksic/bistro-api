package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderToReceiptFormatter implements OrderFormatter<String> {

    @Override
    public String format(Order order) {
        Map<Long, String> productLineMap = order.getItems()
                .stream()
                .collect(Collectors.toMap(
                        OrderItem::getId,
                        orderItem -> String.format("%d x %s @ %.2f = %.2f\n",
                                orderItem.getQuantity(),
                                orderItem.getProduct().getName(),
                                orderItem.getProduct().getPrice(),
                                orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))))
                );
        BigDecimal subtotal = order.getItems()
                .stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = subtotal.subtract(subtotal.multiply(BigDecimal.valueOf(order.getDiscount() / 100)));

        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder
                .append("-------------------------\n")
                .append(String.format("Table Nr. %d\n", order.getId()))
                .append("-------------------------\n");
        for (String productLine : productLineMap.values()) {
            receiptBuilder.append(productLine);
        }
        receiptBuilder
                .append("-------------------------\n")
                .append(String.format("Subtotal: %.2f\n", subtotal))
                .append(String.format("Discount: %d%%\n", order.getDiscount()))
                .append(String.format("Total: %.2f", total));
        return receiptBuilder.toString();
    }
}
