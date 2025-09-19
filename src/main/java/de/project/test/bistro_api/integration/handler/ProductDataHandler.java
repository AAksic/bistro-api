package de.project.test.bistro_api.integration.handler;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataHandler implements GenericHandler<List<Product>> {

    private final ProductRepository productRepository;

    @Override
    public Object handle(List<Product> payload, MessageHeaders headers) {
        payload.forEach(product -> {
            Optional<Product> productWithSameName = productRepository.findByName(product.getName());

            if (productWithSameName.isEmpty()) {
                productRepository.save(product);
                log.info("Successfully saved product {} in the database", product);
            }
        });

        return null;
    }
}
