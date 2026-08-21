package com.kata.books.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public static final Currency EUR = Currency.getInstance("EUR");

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (!EUR.equals(currency)) {
            throw new IllegalArgumentException("Only EUR is supported");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money eur(String amount) {
        return eur(new BigDecimal(amount));
    }

    public static Money eur(BigDecimal amount) {
        return new Money(amount, EUR);
    }
    
    
}
