package com.dev.musinsa.controller.response;

import com.dev.musinsa.database.entity.Product;
import com.dev.musinsa.database.type.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ProductsResponse(
        Long id,
        Category category,
        String brand,
        int price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static List<ProductsResponse> from(List<Product> products) {
        return products.stream()
                .map(ProductsResponse::from)
                .collect(Collectors.toList());

    }

    public static ProductsResponse from(Product product) {
        return new ProductsResponse(
                product.getId(),
                product.getCategory(),
                product.getBrand(),
                product.getPrice(),
                product.getCreatedAt().toLocalDateTime(),
                product.getUpdatedAt().toLocalDateTime()
        );
    }

}