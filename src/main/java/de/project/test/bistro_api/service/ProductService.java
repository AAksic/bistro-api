package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderRequest;

import java.util.List;

public interface ProductService {

    Product getProductById(long productId);

    List<Product> getAllProducts();

    void updateStock(OrderRequest orderRequest);
}
