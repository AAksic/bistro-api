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
        return Order.builder()
                .items(this.mapItems(orderRequest.getOrderItems()))
                .build();
    }


    private List<OrderItem> mapItems(List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests
                .stream()
                .map(orderItemRequest -> OrderItem
                        .builder()
                        .product(productRepository.findById(orderItemRequest.getProductId()).get())
                        .quantity(orderItemRequest.getQuantity())
                        .build())
                .toList();
    }
}
