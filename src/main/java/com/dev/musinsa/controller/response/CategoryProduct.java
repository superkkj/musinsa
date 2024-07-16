package com.dev.musinsa.controller.response;

import com.dev.musinsa.database.entity.Product;
import lombok.Builder;

import java.util.List;

import static com.dev.musinsa.global.utils.PriceFormatter.formatPriceWithCommas;

@Builder
public record CategoryProduct(String category, String brand, String price) {
    public static CategoryProduct from(Product product) {
        return builder()
                .category(product.getCategory().getDisplayName())
                .brand(product.getBrand())
                .price(formatPriceWithCommas(product.getPrice()))
                .build();
    }

    public static List<CategoryProduct> from(List<Product> products) {
        return products
                .stream()
                .map(CategoryProduct::from)
                .toList();
    }
}

