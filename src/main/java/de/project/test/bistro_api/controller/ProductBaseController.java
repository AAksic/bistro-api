package de.project.test.bistro_api.controller;

import de.project.test.bistro_api.domain.Product;
import de.project.test.bistro_api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface ProductBaseController {

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            method = "GET",
            summary = "Retrieves information of all products from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The retrieved product information",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @ResponseStatus(HttpStatus.OK)
    List<Product> getAllProducts();

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            method = "GET",
            summary = "Retrieves information of a product from the database, specified via its id"
    )
    @Parameters(value = {
            @Parameter(name = "id", description = "The id for which to retrieve the product information")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The retrieved product information",
                    content = @Content(schema = @Schema(implementation = Product.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "If the id is not parseable as the type long",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The requested product is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @ResponseStatus(HttpStatus.OK)
    Product getProductById(@PathVariable(name = "id") long productId);
}
