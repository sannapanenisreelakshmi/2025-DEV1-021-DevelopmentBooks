package com.kata.books.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.BookTitle;
import com.kata.books.domain.Money;
import java.math.BigDecimal;

public class UndiscountedBasketPricerTest {
	private final BasketPricer pricer = new UndiscountedBasketPricer();

    @Test
    void pricesAnEmptyBasketAtZeroEuros() {
        assertThat(pricer.price(BookBasket.empty())).isEqualTo(Money.eur("0.00"));
    }
    @Test
    void pricesOneBookAtFiftyEuros() {
        var basket = new BookBasket(Map.of(BookTitle.CLEAN_CODE, 1));

        assertThat(pricer.price(basket)).isEqualTo(Money.eur("50.00"));
    }
    @Test
    void appliesNoDiscountToMultipleCopiesOfOneTitle() {
        var basket = new BookBasket(Map.of(BookTitle.CLEAN_CODE, 3));

        assertThat(pricer.price(basket)).isEqualTo(Money.eur(new BigDecimal("150.00")));
    }
}
