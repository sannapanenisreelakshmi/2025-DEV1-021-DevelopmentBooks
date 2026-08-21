package com.kata.books.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.Test;

public class MoneyTest {
	
	   @Test
	    void rejectsCurrenciesOtherThanEur() {
	        assertThatThrownBy(() -> new Money(BigDecimal.ONE, Currency.getInstance("USD")))
	                .isInstanceOf(IllegalArgumentException.class)
	                .hasMessage("Only EUR is supported");
	    } 
	   @Test
	    void roundsEuroAmountsToTwoDecimalsUsingHalfUp() {
	        assertThat(Money.eur("1.005").amount()).isEqualByComparingTo("1.01");
	    }

}
