package com.kata.books.infrastructure.web;

import java.math.BigDecimal;

public record BasketPriceResponse(String currency, BigDecimal total) {

}
