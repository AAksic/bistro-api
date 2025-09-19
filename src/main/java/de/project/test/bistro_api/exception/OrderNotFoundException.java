package de.project.test.bistro_api.exception;

public class OrderNotFoundException extends CustomNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
