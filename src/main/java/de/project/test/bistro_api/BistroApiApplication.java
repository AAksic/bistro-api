package de.project.test.bistro_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class BistroApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BistroApiApplication.class, args);
    }

}
