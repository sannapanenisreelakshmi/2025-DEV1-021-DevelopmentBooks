package com.kata.books.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class BookBasketTest {
	
	@Test
    void acceptsAnEmptyBasket() {
        assertThat(BookBasket.empty().quantities()).isEmpty();
    }
	
	@Test
	void rejectsZeroOrNegativeQuantities() {
	    assertThatThrownBy(() -> new BookBasket(Map.of(BookTitle.CLEAN_CODE, 0)))
	             .isInstanceOf(IllegalArgumentException.class)
	             .hasMessage("Book quantities must be positive");
	    }
	 @Test
	    void defensivelyCopiesQuantities() {
	        var basket = new BookBasket(Map.of(BookTitle.CLEAN_CODE, 2));

	        assertThat(basket.quantities()).containsExactly(Map.entry(BookTitle.CLEAN_CODE, 2));
	        assertThatThrownBy(() -> basket.quantities().put(BookTitle.CLEAN_CODER, 1))
	                .isInstanceOf(UnsupportedOperationException.class);
	    }
}
