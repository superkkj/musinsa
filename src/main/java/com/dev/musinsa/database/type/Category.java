package com.dev.musinsa.database.type;

import com.dev.musinsa.exception.ErrorCode;
import com.dev.musinsa.exception.advice.CommonException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("바지"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    HAT("모자"),
    SOCKS("양말"),
    ACCESSORY("액세서리");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public static Category fromDisplayName(String displayName) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CATEGORY));
    }

}