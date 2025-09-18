package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;

public interface OrderService {

    Order placeOrder(OrderRequest order);

    String getOrderById(long orderId);
}
