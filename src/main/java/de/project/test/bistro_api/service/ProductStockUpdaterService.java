package de.project.test.bistro_api.service;

import de.project.test.bistro_api.dto.OrderRequest;

public interface ProductStockUpdaterService {

    void updateStock(OrderRequest orderRequest);
}
