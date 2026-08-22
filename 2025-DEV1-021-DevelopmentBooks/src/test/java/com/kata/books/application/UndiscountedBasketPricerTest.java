package com.kata.books.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.Money;

public class UndiscountedBasketPricerTest {
	private final BasketPricer pricer = new UndiscountedBasketPricer();

    @Test
    void pricesAnEmptyBasketAtZeroEuros() {
        assertThat(pricer.price(BookBasket.empty())).isEqualTo(Money.eur("0.00"));
    }

}
