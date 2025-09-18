package de.project.test.bistro_api.integration.handler;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.core.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDataCsvHandler implements GenericHandler<Product> {

    private final ProductRepository productRepository;

    @Override
    public Object handle(Product payload, MessageHeaders headers) {
        productRepository.save(payload);
        return payload;
    }
}
