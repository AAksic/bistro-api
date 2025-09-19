package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.OrderNotFoundException;
import de.project.test.bistro_api.mapper.OrderRequestMapper;
import de.project.test.bistro_api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRequestMapper orderRequestMapper;
    private final OrderRepository orderRepository;
    private final OrderFormatter<String> orderFormatter;

    @Override
    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {
        Order mappedOrder = orderRequestMapper.toOrder(orderRequest);

        return orderRepository.save(mappedOrder);
    }

    @Override
    public String getOrderById(long orderId) {
        Order retrievedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d does not exist", orderId)));

        return orderFormatter.format(retrievedOrder);
    }
}
