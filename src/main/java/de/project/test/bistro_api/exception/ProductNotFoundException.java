package de.project.test.bistro_api.exception;

public class ProductNotFoundException extends CustomNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
