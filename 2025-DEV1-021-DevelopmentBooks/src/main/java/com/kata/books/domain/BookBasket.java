package com.kata.books.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record BookBasket(Map<BookTitle, Integer> quantities) {
	
	public BookBasket {
        Objects.requireNonNull(quantities, "quantities must not be null");
        var copiedQuantities = new EnumMap<BookTitle, Integer>(BookTitle.class);
        quantities.forEach((title, quantity) -> {
            Objects.requireNonNull(title, "book title must not be null");
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Book quantities must be positive");
            }
            copiedQuantities.put(title, quantity);
        });
        quantities = Map.copyOf(copiedQuantities);
    }
	    public static BookBasket empty() {
	        return new BookBasket(Map.of());
	    }
   
}