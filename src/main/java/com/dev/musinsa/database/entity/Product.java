package com.dev.musinsa.database.entity;

import com.dev.musinsa.database.type.Category;
import com.dev.musinsa.service.cmd.ProductAddCommand;
import com.dev.musinsa.service.cmd.ProductUpdateCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar")
    private Category category;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private int price;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Version
    private Long version;


    public static Product from(ProductAddCommand cmd) {
        return Product.builder()
                .category(cmd.category())
                .brand(cmd.brand())
                .price(cmd.price())
                .build();
    }

    public void update(ProductUpdateCommand cmd) {
        this.category = cmd.category();
        this.brand = cmd.brand();
        this.price = cmd.price();
    }
}