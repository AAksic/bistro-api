package de.project.test.bistro_api.dto;

import de.project.test.bistro_api.validation.ProductsExistConstraint;
import de.project.test.bistro_api.validation.ProductsInStockConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequest {
    @ProductsExistConstraint
    @ProductsInStockConstraint
    private List<OrderItemRequest> orderItems;
}
