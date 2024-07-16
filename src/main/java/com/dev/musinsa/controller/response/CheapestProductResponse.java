package com.dev.musinsa.controller.response;

import com.dev.musinsa.database.entity.Product;
import lombok.Builder;

import java.util.List;

@Builder
public record CheapestProductResponse(List<CategoryProduct> products, String totalPrice) {
    public static CheapestProductResponse from(List<Product> cheaperProducts, String totalPrice) {
        return CheapestProductResponse
                .builder()
                .products(CategoryProduct.from(cheaperProducts))
                .totalPrice(totalPrice)
                .build();
    }
}