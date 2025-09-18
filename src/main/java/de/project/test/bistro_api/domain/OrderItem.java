package de.project.test.bistro_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orderitems")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {

    @Id
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;
}
