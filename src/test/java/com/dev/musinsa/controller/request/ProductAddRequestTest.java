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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductAddRequest 유효성 검사")
class ProductAddRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효한 ProductAddRequest 생성")
    void validProductAddRequest() {
        ProductAddRequest request = new ProductAddRequest(Category.TOP, "BrandA", 1000);
        Set<ConstraintViolation<ProductAddRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "유효한 요청에 대해 위반사항이 없어야 합니다.");
    }

    @Test
    @DisplayName("카테고리가 null일 때 유효성 검사 실패")
    void invalidCategory() {
        ProductAddRequest request = new ProductAddRequest(null, "BrandA", 1000);
        Set<ConstraintViolation<ProductAddRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "카테고리가 null일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("카테고리는 필수 입력값입니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }

    @Test
    @DisplayName("브랜드가 빈 문자열일 때 유효성 검사 실패")
    void invalidBrand() {
        ProductAddRequest request = new ProductAddRequest(Category.TOP, "", 1000);
        Set<ConstraintViolation<ProductAddRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "브랜드가 빈 문자열일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("브랜드는 필수 입력값입니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    @DisplayName("가격이 음수일 때 유효성 검사 실패")
    void invalidPrice(int price) {
        ProductAddRequest request = new ProductAddRequest(Category.TOP, "BrandA", price);
        Set<ConstraintViolation<ProductAddRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size(), "가격이 음수일 때 하나의 위반사항이 있어야 합니다.");
        assertEquals("가격은 0 이상이어야 합니다.", violations.iterator().next().getMessage(), "올바른 오류 메시지가 반환되어야 합니다.");
    }
}