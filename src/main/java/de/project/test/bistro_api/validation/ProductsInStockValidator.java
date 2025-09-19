package de.project.test.bistro_api.validation;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductsInStockValidator implements ConstraintValidator<ProductsInStockConstraint, List<OrderItemRequest>> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(List<OrderItemRequest> orderItemRequests, ConstraintValidatorContext constraintValidatorContext) {
        return orderItemRequests
                .stream()
                .noneMatch(orderItemRequest -> {
                    Optional<Product> retrievedProduct = productRepository.findById(orderItemRequest.getProductId());

                    return retrievedProduct.isEmpty() || retrievedProduct.get().getStock() < orderItemRequest.getQuantity();
                });
    }
}
