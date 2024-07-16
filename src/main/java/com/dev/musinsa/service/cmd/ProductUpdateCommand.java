package com.dev.musinsa.service.cmd;

import com.dev.musinsa.database.type.Category;

public record ProductUpdateCommand(
        Category category,
        String brand,
        Integer price
) {

    public static ProductUpdateCommand from(Category category, String brand, Integer price) {
        return new ProductUpdateCommand(category, brand, price);
    }
}