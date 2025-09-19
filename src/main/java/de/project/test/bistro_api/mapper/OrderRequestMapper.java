package de.project.test.bistro_api.mapper;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderRequestMapper {

    private final ProductRepository productRepository;

    public Order toOrder(OrderRequest orderRequest) {
        Order createdOrder = new Order();
        createdOrder.setItems(this.mapItems(orderRequest.getOrderItems(), createdOrder));
        return createdOrder;
    }


    private List<OrderItem> mapItems(List<OrderItemRequest> orderItemRequests, Order orderToMapTo) {
        return orderItemRequests
                .stream()
                .map(orderItemRequest -> OrderItem
                        .builder()
                        .product(productRepository.findById(orderItemRequest.getProductId()).get())
                        .quantity(orderItemRequest.getQuantity())
                        .order(orderToMapTo)
                        .build())
                .toList();
    }
}
