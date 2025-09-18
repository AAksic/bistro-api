package de.project.test.bistro_api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@JsonPropertyOrder({
        "name",
        "quantity",
        "price"
})
public class Product {

    @Builder
    public Product() {
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, scale = 2)
    private BigDecimal price;
}
