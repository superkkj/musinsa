package com.dev.musinsa.domain;

import com.dev.musinsa.controller.response.CategoryPrice;
import com.dev.musinsa.database.entity.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dev.musinsa.global.utils.PriceFormatter.formatPriceWithCommas;

public record ProductCollection(List<Product> products) {

    public ProductCollection {
        products = products == null ? List.of() : products;
    }

    public List<Product> getCheapestProductsByCategory() {
        return new ArrayList<>(products.stream()
                .collect(Collectors.toMap(
                        Product::getCategory,
                        Function.identity(),
                        (p1, p2) -> p1.getPrice() <= p2.getPrice() ? p1 : p2,
                        LinkedHashMap::new
                ))
                .values());
    }

    public String calculateTotalPrice(List<Product> cheaperProducts) {
        int totalPrice = cheaperProducts
                .stream()
                .mapToInt(Product::getPrice)
                .sum();
        return formatPriceWithCommas(totalPrice);
    }

    public List<CategoryPrice> getCategoryPrices() {
        return products.stream()
                .map(p -> new CategoryPrice(p.getCategory().getDisplayName(), formatPriceWithCommas(p.getPrice())))
                .collect(Collectors.toList());
    }


    public Product getCheapestProduct() {
        return products.stream()
                .min(Comparator.comparingInt(Product::getPrice))
                .orElseThrow(() -> new IllegalStateException("No products available"));
    }

    public Product getMostExpensiveProduct() {
        return products.stream()
                .max(Comparator.comparingInt(Product::getPrice))
                .orElseThrow(() -> new IllegalStateException("No products available"));
    }
}