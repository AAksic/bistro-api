package de.project.test.bistro_api.validation;

import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductsExistValidator implements ConstraintValidator<ProductsExistConstraint, List<OrderItemRequest>> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(List<OrderItemRequest> orderitems, ConstraintValidatorContext constraintValidatorContext) {
        return orderitems
                .stream()
                .noneMatch(orderItemRequest -> productRepository.findById(orderItemRequest.getProductId()).isEmpty());
    }
}
