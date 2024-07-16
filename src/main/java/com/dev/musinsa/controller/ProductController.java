package com.dev.musinsa.controller;

import com.dev.musinsa.controller.request.ProductAddRequest;
import com.dev.musinsa.controller.request.ProductUpdateRequest;
import com.dev.musinsa.controller.response.*;
import com.dev.musinsa.service.ProductService;
import com.dev.musinsa.service.cmd.ProductAddCommand;
import com.dev.musinsa.service.cmd.ProductUpdateCommand;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductService service;


    @Operation(summary = "모든 상품 조회", description = "화면 표시용 모든 상품 목록을 조회합니다.")
    @GetMapping
    public CommonResponse<List<ProductsResponse>> getProducts() {
        return CommonResponse.success(service.getProducts());
    }

    @Operation(summary = "최저가 코디 조회", description = "각 카테고리별 최저가 상품과 총 가격을 조회합니다.")
    @GetMapping("/cheapest-outfit")
    public CommonResponse<CheapestProductResponse> getCheapestOutfit() {
        return CommonResponse.success(service.getCheapestProductsByCategory());
    }

    @Operation(summary = "최저가 브랜드 조회", description = "모든 카테고리 상품을 가장 저렴하게 구매할 수 있는 브랜드와 총 가격을 조회합니다.")
    @GetMapping("/lowest-price-brand")
    public CommonResponse<LowestPriceBrandResponse> getLowestPriceBrand() {
        return CommonResponse.success(service.findLowestPriceBrand());
    }

    @Operation(summary = "카테고리별 가격 범위 조회", description = "특정 카테고리의 최저가와 최고가 브랜드 및 가격을 조회합니다.")
    @GetMapping("/categories/{categoryName}/price-range")
    public CommonResponse<CategoryPriceRangeResponse> getCategoryPriceRange(@PathVariable String categoryName) {
        return CommonResponse.success(service.getCategoryPriceRange(categoryName));
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping
    public CommonResponse<Long> addProduct(@RequestBody @Valid ProductAddRequest request) {
        return CommonResponse.success(service.addProduct(
                        ProductAddCommand.from(
                                request.category(),
                                request.brand(),
                                request.price()
                        )
                )
        );
    }

    @Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다.")
    @PutMapping("/{id}")
    public CommonResponse updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request) {
        service.update(id, ProductUpdateCommand.from(request.category(), request.brand(), request.price()));
        return CommonResponse.success();
    }

    @Operation(summary = "상품 삭제", description = "지정된 ID의 상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public CommonResponse deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return CommonResponse.success();
    }
}