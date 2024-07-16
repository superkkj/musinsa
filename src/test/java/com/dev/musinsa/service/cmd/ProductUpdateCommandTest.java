package com.dev.musinsa.service.cmd;

import com.dev.musinsa.database.type.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ProductUpdateCommand 테스트")
class ProductUpdateCommandTest {

    @Test
    @DisplayName("ProductUpdateCommand 객체 생성 테스트")
    void createProductUpdateCommand() {
        Category category = Category.TOP;
        String brand = "BrandA";
        int price = 1000;

        ProductUpdateCommand command = new ProductUpdateCommand(category, brand, price);

        assertNotNull(command, "ProductUpdateCommand 객체가 생성되어야 합니다.");
        assertEquals(category, command.category(), "설정한 카테고리와 일치해야 합니다.");
        assertEquals(brand, command.brand(), "설정한 브랜드와 일치해야 합니다.");
        assertEquals(price, command.price(), "설정한 가격과 일치해야 합니다.");
    }

    @Test
    @DisplayName("from 메서드를 사용한 ProductUpdateCommand 객체 생성 테스트")
    void createProductUpdateCommandUsingFromMethod() {
        Category category = Category.OUTER;
        String brand = "BrandB";
        int price = 2000;

        ProductUpdateCommand command = ProductUpdateCommand.from(category, brand, price);

        assertNotNull(command, "ProductUpdateCommand 객체가 생성되어야 합니다.");
        assertEquals(category, command.category(), "설정한 카테고리와 일치해야 합니다.");
        assertEquals(brand, command.brand(), "설정한 브랜드와 일치해야 합니다.");
        assertEquals(price, command.price(), "설정한 가격과 일치해야 합니다.");
    }
}