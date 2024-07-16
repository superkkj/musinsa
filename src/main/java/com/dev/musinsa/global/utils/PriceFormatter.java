package com.dev.musinsa.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PriceFormatter {
    public static String formatPriceWithCommas(int price) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }
}