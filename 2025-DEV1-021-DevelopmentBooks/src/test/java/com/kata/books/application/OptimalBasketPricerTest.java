package com.kata.books.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.BookTitle;
import com.kata.books.domain.Money;

public class OptimalBasketPricerTest {
	private final BasketPricer pricer = new OptimalBasketPricer();

    @Test
    void appliesEveryDistinctSetDiscount() {
        assertThat(price(1, 0, 0, 0, 0)).isEqualTo(Money.eur("50.00"));
        assertThat(price(1, 1, 0, 0, 0)).isEqualTo(Money.eur("95.00"));
        assertThat(price(1, 1, 1, 0, 0)).isEqualTo(Money.eur("135.00"));
        assertThat(price(1, 1, 1, 1, 0)).isEqualTo(Money.eur("160.00"));
        assertThat(price(1, 1, 1, 1, 1)).isEqualTo(Money.eur("187.50"));
    }
    @Test
    void pricesRepeatedDiscountedSets() {
        assertThat(price(2, 2, 0, 0, 0)).isEqualTo(Money.eur("190.00"));
        assertThat(price(3, 3, 3, 3, 3)).isEqualTo(Money.eur("562.50"));
    }
    @Test
    void choosesTwoFourTitleSetsInsteadOfAFiveAndAThreeTitleSet() {
        assertThat(price(2, 2, 2, 1, 1)).isEqualTo(Money.eur("320.00"));
    }
    @Test
    void producesTheSamePriceRegardlessOfEntryOrder() {
        var first = new BookBasket(Map.of(BookTitle.CLEAN_CODE, 2, BookTitle.CLEAN_CODER, 1,
                BookTitle.CLEAN_ARCHITECTURE, 2));
        var second = new BookBasket(Map.of(BookTitle.CLEAN_ARCHITECTURE, 2, BookTitle.CLEAN_CODE, 2,
                BookTitle.CLEAN_CODER, 1));

        assertThat(pricer.price(first)).isEqualTo(pricer.price(second));
    }
    private Money price(int cleanCode, int cleanCoder, int architecture, int tdd, int legacyCode) {
        var quantities = new EnumMap<BookTitle, Integer>(BookTitle.class);
        add(quantities, BookTitle.CLEAN_CODE, cleanCode);
        add(quantities, BookTitle.CLEAN_CODER, cleanCoder);
        add(quantities, BookTitle.CLEAN_ARCHITECTURE, architecture);
        add(quantities, BookTitle.TDD_BY_EXAMPLE, tdd);
        add(quantities, BookTitle.WORKING_EFFECTIVELY_WITH_LEGACY_CODE, legacyCode);
        return pricer.price(new BookBasket(quantities));
    }
    private void add(Map<BookTitle, Integer> quantities, BookTitle title, int quantity) {
        if (quantity > 0) {
            quantities.put(title, quantity);
        }
    }

}
