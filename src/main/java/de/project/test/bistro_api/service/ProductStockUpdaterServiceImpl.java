package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockUpdaterServiceImpl implements ProductStockUpdaterService {

    private final ProductRepository productRepository;

    @Override
    public void updateStock(OrderRequest orderRequest) {
        orderRequest.getOrderItems()
                .forEach(orderItem -> {
                    Product orderedProduct = productRepository.findById(orderItem.getProductId()).get();
                    int currentStock = orderedProduct.getStock();
                    int newStock = currentStock - orderItem.getQuantity();
                    orderedProduct.setStock(newStock);
                    productRepository.save(orderedProduct);
                });
    }
}
