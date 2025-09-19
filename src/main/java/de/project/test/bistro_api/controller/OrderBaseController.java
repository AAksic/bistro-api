package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Order;
import de.project.test.bistro_api.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface OrderBaseController {

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            method = "POST",
            summary = "Places a new order"
    )
    @Parameters(value = {
            @Parameter(name = "orderRequest", description = "The order request that should be placed")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The order was successfully placed",
                    content = @Content(schema = @Schema(implementation = Order.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The order is not in the required format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    Order placeOrder(@RequestBody @Valid OrderRequest orderRequest);


    @GetMapping(
            path = "/{id}",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @Operation(
            method = "GET",
            summary = "Retrieves information of the order from the database, specified by its id"
    )
    @Parameters(value = {
            @Parameter(name = "id", description = "The id for which to retrieve the order information")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The retrieved order information",
                    content = @Content(examples = @ExampleObject(name = "receipt", value = """
                            -------------------------
                            Table Nr. 10
                            -------------------------
                            2 x Pizza @ 10.0 = 20.0
                            3 x Cola  @ 2.5  = 7.5
                            -------------------------
                            Subtotal: 27.5
                            Discount: 10%
                            Total: 24.75
                            """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "If the id is not parseable as the type long",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The requested order is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @ResponseStatus(HttpStatus.OK)
    String getOrderById(@PathVariable(name = "id") long orderId);

}
