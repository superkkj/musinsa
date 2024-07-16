package com.dev.musinsa.controller.response;

import com.dev.musinsa.database.entity.Product;

import static com.dev.musinsa.global.utils.PriceFormatter.formatPriceWithCommas;

public record BrandPrice(String brand, String price) {

    public static BrandPrice from(Product product) {
        return new BrandPrice(product.getBrand(), formatPriceWithCommas(product.getPrice()));
    }
}