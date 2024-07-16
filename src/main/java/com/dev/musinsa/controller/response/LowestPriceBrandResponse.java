package com.dev.musinsa.controller.response;

import lombok.Builder;

import java.util.List;

import static com.dev.musinsa.global.utils.PriceFormatter.formatPriceWithCommas;


@Builder
public record LowestPriceBrandResponse(String brand, List<CategoryPrice> categories, String totalPrice) {

    public static LowestPriceBrandResponse from(String brand, List<CategoryPrice> categoryPrices, Long totalPrice) {

        return LowestPriceBrandResponse
                .builder()
                .brand(brand)
                .categories(categoryPrices)
                .totalPrice(formatPriceWithCommas(totalPrice.intValue()))
                .build();
    }
}
