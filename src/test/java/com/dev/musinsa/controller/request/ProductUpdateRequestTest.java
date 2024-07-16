package com.dev.musinsa.controller.request;

import com.dev.musinsa.database.type.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductUpdateRequest 유효성 검사")
class ProductUpdateRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효한 ProductUpdateRequest 생성")
    void validProductUpdateRequest() {
        ProductUpdateRequest request = ProductUpdateRequest.from(Category.TOP, "BrandA", 1000);
        Set<ConstraintViolation<ProductUpdateRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "유효한 요청에 대해 위반사항이 없어야 합니다.");
    }

    @Test
    @DisplayName("카테고리가 null일 때 유효성 검사 실패")
    void invalidCategory() {
        ProductUpdateRequest request = ProductUpdateRequest.from(null, "BrandA", 1000);
        Set<ConstraintViolation<ProductUpdateRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "카테고리가 null일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("카테고리는 필수 입력값입니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }

    @Test
    @DisplayName("브랜드가 빈 문자열일 때 유효성 검사 실패")
    void invalidBrand() {
        ProductUpdateRequest request = ProductUpdateRequest.from(Category.TOP, "", 1000);
        Set<ConstraintViolation<ProductUpdateRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "브랜드가 빈 문자열일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("브랜드는 필수 입력값입니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    @DisplayName("가격이 음수일 때 유효성 검사 실패")
    void invalidPrice(int price) {
        ProductUpdateRequest request = ProductUpdateRequest.from(Category.TOP, "BrandA", price);
        Set<ConstraintViolation<ProductUpdateRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "가격이 음수일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("가격은 0 이상이어야 합니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }

    @Test
    @DisplayName("from 메서드로 유효한 ProductUpdateRequest 생성")
    void fromMethodCreatesValidRequest() {
        ProductUpdateRequest request = ProductUpdateRequest.from(Category.TOP, "BrandA", 1000);
        assertNotNull(request, "생성된 요청 객체는 null이 아니어야 합니다.");
        assertEquals(Category.TOP, request.category(), "카테고리가 올바르게 설정되어야 합니다.");
        assertEquals("BrandA", request.brand(), "브랜드가 올바르게 설정되어야 합니다.");
        assertEquals(1000, request.price(), "가격이 올바르게 설정되어야 합니다.");
    }
}