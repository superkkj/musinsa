package com.dev.musinsa.service.cmd;

import com.dev.musinsa.database.type.Category;

public record ProductAddCommand(
        Category category,
        String brand,
        Integer price
) {

    public static ProductAddCommand from(Category category, String brand, Integer price) {
        return new ProductAddCommand(category, brand, price);
    }
}