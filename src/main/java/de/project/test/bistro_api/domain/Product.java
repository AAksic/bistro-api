package de.project.test.bistro_api.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({
        "name",
        "price",
        "stock"
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, scale = 2)
    private BigDecimal price;
}
