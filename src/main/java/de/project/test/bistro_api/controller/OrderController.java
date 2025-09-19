package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.service.OrderService;
import de.project.test.bistro_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController implements OrderBaseController {

    private final OrderService orderService;
    private final ProductService productService;

    @Override
    public Order placeOrder(OrderRequest orderRequest) {
        Order placedOrder = orderService.placeOrder(orderRequest);
        productService.updateStock(orderRequest);

        return placedOrder;
    }

    @Override
    public String getOrderById(long orderId) {
        return orderService.getOrderById(orderId);
    }
}
