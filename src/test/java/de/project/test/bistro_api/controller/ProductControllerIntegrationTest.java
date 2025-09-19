package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    @LocalServerPort
    int applicationPort;

    @Autowired
    private ProductController unitUnderTest;

    @Test
    @Sql(scripts = {"/sql/add_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/cleanup_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getProductById_success() {
        Product retrievedProduct = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/products/{id}", 1)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getObject(".", Product.class);

        assertEquals(1, retrievedProduct.getId());
        assertEquals("Pizza", retrievedProduct.getName());
        assertEquals(BigDecimal.valueOf(10.25), retrievedProduct.getPrice());
        assertEquals(50, retrievedProduct.getStock());
    }

    @Test
    void getProductById_failure_productWithIdDoesNotExist() {
        ErrorResponse errorResponse = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/products/{id}", 1)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .extract().body().jsonPath().getObject(".", ErrorResponse.class);

        assertEquals("ProductNotFoundException", errorResponse.getExceptionType());
        assertEquals("Product with id 1 does not exist", errorResponse.getMessage());
    }

    @Test
    @Sql(scripts = {"/sql/add_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/cleanup_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllProducts_success() {
        List<Product> retrievedProducts = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/products")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Product.class);

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
        List<Product> retrievedProducts = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/products")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Product.class);

        assertThat(retrievedProducts).hasSize(0);
    }
}
