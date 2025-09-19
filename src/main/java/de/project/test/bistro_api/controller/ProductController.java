package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController implements ProductBaseController {

    private final ProductService productService;

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public Product getProductById(@PathVariable(name = "id") long productId) {
        return productService.getProductById(productId);
    }
}
