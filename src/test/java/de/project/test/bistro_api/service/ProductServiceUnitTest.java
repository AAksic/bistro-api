package de.project.test.bistro_api.service;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.exception.ProductNotFoundException;
import de.project.test.bistro_api.factory.MockDataFactory;
import de.project.test.bistro_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl unitUnderTest;


    @Test
    void getProductById_success() {
        Product mockProduct = MockDataFactory.generateMockProduct();

        when(productRepository.findById(eq(1L)))
                .thenReturn(Optional.of(mockProduct));

        Product retrievedProduct = unitUnderTest.getProductById(1);

        assertEquals(1, retrievedProduct.getId());
        assertEquals("Wiener Schnitzel", retrievedProduct.getName());
        assertEquals(BigDecimal.valueOf(17.50), retrievedProduct.getPrice());
        assertEquals(35, retrievedProduct.getStock());
    }

    @Test
    void getProductById_failure_productWithIdDoesNotExist() {
        String expectedErrorMessage = "Product with id 1 does not exist";

        when(productRepository.findById(eq(1L)))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> unitUnderTest.getProductById(1));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void getAllProducts_success() {
        List<Product> mockProductsWithIds = MockDataFactory.generateMockProductsWithIds();

        when(productRepository.findAll())
                .thenReturn(mockProductsWithIds);

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
        assertEquals(BigDecimal.valueOf(2.5), second.getPrice());
        assertEquals(200, second.getStock());

        Product third = retrievedProducts.get(2);
        assertEquals(3, third.getId());
        assertEquals("Water", third.getName());
        assertEquals(BigDecimal.valueOf(1.5), third.getPrice());
        assertEquals(1000, third.getStock());

        Product fourth = retrievedProducts.get(3);
        assertEquals(4, fourth.getId());
        assertEquals("Chicken Tikka Masala", fourth.getName());
        assertEquals(BigDecimal.valueOf(12.75), fourth.getPrice());
        assertEquals(15, fourth.getStock());
    }

    @Test
    void getAllProducts_success_databaseIsEmpty() {
        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Product> retrievedProducts = unitUnderTest.getAllProducts();

        assertThat(retrievedProducts).hasSize(0);
    }
}
