package com.kata.books.application;

import java.math.BigDecimal;
import java.util.Map;

/** Discount rates indexed by the number of distinct titles in a set. */
public final class DiscountPolicy {

    private static final Map<Integer, BigDecimal> RATES = Map.of(
            1, new BigDecimal("0.00"),
            2, new BigDecimal("0.05"),
            3, new BigDecimal("0.10"),
            4, new BigDecimal("0.20"),
            5, new BigDecimal("0.25"));

    public BigDecimal discountFor(int distinctTitles) {
        var rate = RATES.get(distinctTitles);
        if (rate == null) {
            throw new IllegalArgumentException("A set must contain between one and five distinct titles");
        }
        return rate;
    }
}