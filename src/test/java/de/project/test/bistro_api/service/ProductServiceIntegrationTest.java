package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.exception.ProductNotFoundException;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl unitUnderTest;

    @BeforeEach
    void setup() {
        productRepository.saveAll(MockDataFactory.generateMockProducts());
    }

    @Test
    void getProductById_success() {
        Product retrievedProduct = unitUnderTest.getProductById(1);

        assertEquals(1, retrievedProduct.getId());
        assertEquals("Pizza", retrievedProduct.getName());
        assertEquals(BigDecimal.valueOf(10.25), retrievedProduct.getPrice());
        assertEquals(50, retrievedProduct.getStock());
    }

    @Test
    void getProductById_failure_productWithIdDoesNotExist() {
        assertThrows(ProductNotFoundException.class, () -> unitUnderTest.getProductById(500));
    }

    @Test
    void getAllProducts_success() {
        List<Product> retrievedProducts = unitUnderTest.getAllProducts();

        assertThat(retrievedProducts).hasSize(4);

        Product first = retrievedProducts.getFirst();
        assertEquals(1, first.getId());
        assertEquals("Pizza", first.getName());
        assertEquals(BigDecimal.valueOf(10.25), first.getPrice());
        assertEquals(50, first.getStock());

        Product second = retrievedProducts.get(1);
        assertEquals(2, second.getId());
        assertEquals("Cola", second.getName());
        assertEquals(BigDecimal.valueOf(2.50).setScale(2, RoundingMode.UNNECESSARY), second.getPrice());
        assertEquals(200, second.getStock());

        Product third = retrievedProducts.get(2);
        assertEquals(3, third.getId());
        assertEquals("Water", third.getName());
        assertEquals(BigDecimal.valueOf(1.5).setScale(2, RoundingMode.UNNECESSARY), third.getPrice());
        assertEquals(1000, third.getStock());

        Product fourth = retrievedProducts.get(3);
        assertEquals(4, fourth.getId());
        assertEquals("Chicken Tikka Masala", fourth.getName());
        assertEquals(BigDecimal.valueOf(12.75), fourth.getPrice());
        assertEquals(15, fourth.getStock());
    }

    @Test
    void getAllProducts_success_databaseIsEmpty() {
        productRepository.deleteAll();

        List<Product> retrievedProducts = unitUnderTest.getAllProducts();

        assertThat(retrievedProducts).hasSize(0);
    }
}
