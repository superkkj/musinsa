package com.dev.musinsa.controller.response;

import com.dev.musinsa.database.entity.Product;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryPriceRangeResponse(
        String category,
        List<BrandPrice> lowestPrices,
        List<BrandPrice> highestPrices
) {
    public static CategoryPriceRangeResponse from(String categoryName, Product cheapestProduct, Product mostExpensiveProduct) {
        return CategoryPriceRangeResponse
                .builder()
                .category(categoryName)
                .lowestPrices(List.of(BrandPrice.from(cheapestProduct)))
                .highestPrices(List.of(BrandPrice.from(mostExpensiveProduct)))
                .build();
    }
}