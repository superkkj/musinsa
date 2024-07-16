package com.dev.musinsa.controller.request;

import com.dev.musinsa.database.type.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductUpdateRequest(
        @NotNull(message = "카테고리는 필수 입력값입니다.")
        Category category,
        @NotBlank(message = "브랜드는 필수 입력값입니다.")
        String brand,
        @NotNull(message = "가격은 필수 입력값입니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int price
) {
    public static ProductUpdateRequest from(Category category, String brand, Integer price) {
        return ProductUpdateRequest
                .builder()
                .category(category)
                .brand(brand)
                .price(price)
                .build();
    }
}