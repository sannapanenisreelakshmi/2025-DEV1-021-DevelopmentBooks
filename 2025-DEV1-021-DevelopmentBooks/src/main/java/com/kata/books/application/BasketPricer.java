package com.kata.books.application;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.Money;

public interface BasketPricer {
	  Money price(BookBasket basket);

}
