package com.kata.books.application;

import java.math.BigDecimal;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.Money;

public final class UndiscountedBasketPricer implements BasketPricer {

    private static final BigDecimal UNIT_PRICE = new BigDecimal("50.00");

    @Override
    public Money price(BookBasket basket) {
        return Money.eur(UNIT_PRICE.multiply(BigDecimal.valueOf(basket.totalBooks())));
    }
}

