package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.domain.OrderItem;
import de.project.test.bistro_api.dto.OrderItemRequest;
import de.project.test.bistro_api.dto.OrderRequest;
import de.project.test.bistro_api.exception.ErrorResponse;
import io.restassured.http.ContentType;
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
class OrderControllerIntegrationTest {

    @LocalServerPort
    int applicationPort;

    @Autowired
    private OrderController unitUnderTest;

    @Test
    @Sql(scripts = {"/sql/add_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/cleanup_test_data_order.sql", "/sql/cleanup_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void placeOrder_success() {
        OrderRequest orderRequest = OrderRequest.builder()
                .orderItems(List.of(
                        OrderItemRequest.builder()
                                .productId(1L)
                                .quantity(1)
                                .build(),
                        OrderItemRequest.builder()
                                .productId(2L)
                                .quantity(4)
                                .build()
                ))
                .build();

        Order retrievedOrder = given()
                .port(applicationPort)
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getObject(".", Order.class);

        assertEquals(1L, retrievedOrder.getId());
        assertThat(retrievedOrder.getItems()).hasSize(2);

        OrderItem first = retrievedOrder.getItems().getFirst();
        assertEquals(1L, first.getId());
        assertEquals(1, first.getQuantity());
        assertEquals(1L, first.getProduct().getId());
        assertEquals("Pizza", first.getProduct().getName());
        assertEquals(BigDecimal.valueOf(10.25), first.getProduct().getPrice());
        assertEquals(49, first.getProduct().getStock());

        OrderItem second = retrievedOrder.getItems().get(1);
        assertEquals(2L, second.getId());
        assertEquals(4, second.getQuantity());
        assertEquals(2L, second.getProduct().getId());
        assertEquals("Cola", second.getProduct().getName());
        assertEquals(BigDecimal.valueOf(2.50), second.getProduct().getPrice());
        assertEquals(196, second.getProduct().getStock());
    }

    @Test
    void placeOrder_failure_unmappableRequest() {
        given()
                .port(applicationPort)
                .body("something_that_fails")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = {"/sql/add_test_data_product.sql", "/sql/add_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/cleanup_test_data_order.sql", "/sql/cleanup_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOrderById_success() {
        String retrievedOrder = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/orders/{id}", 1L)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().asString();

        assertThat(retrievedOrder).contains("1 x Pizza @ 10,25 = 10,25");
        assertThat(retrievedOrder).contains("4 x Cola @ 2,50 = 10,00");
        assertThat(retrievedOrder).contains("Subtotal: 20,25");
        assertThat(retrievedOrder).contains("Discount: 0%");
        assertThat(retrievedOrder).contains("Total: 20,25");
    }

    @Test
    void getOrderById_failure_orderWithIdDoesNotExist() {
        ErrorResponse errorResponse = given()
                .port(applicationPort)
                .log().all()
                .when()
                .get("/orders/{id}", 1L)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .extract().body().jsonPath().getObject(".", ErrorResponse.class);

        assertEquals("OrderNotFoundException", errorResponse.getExceptionType());
        assertEquals("Order with id 1 does not exist", errorResponse.getMessage());
    }

}