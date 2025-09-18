package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController implements OrderBaseController {

    private final OrderService orderService;

    @Override
    public Order placeOrder(OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @Override
    public String getOrderById(long orderId) {
        return orderService.getOrderById(orderId);
    }
}
