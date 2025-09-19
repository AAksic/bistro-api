package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.ProductNotFoundException;
import de.project.test.bistro_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d does not exist", productId)));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

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
