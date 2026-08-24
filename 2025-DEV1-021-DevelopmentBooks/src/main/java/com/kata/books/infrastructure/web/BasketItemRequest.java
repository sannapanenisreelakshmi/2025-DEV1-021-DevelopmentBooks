package com.kata.books.infrastructure.web;

import com.kata.books.domain.BookTitle;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BasketItemRequest(
        @NotNull(message = "title is required") BookTitle title,
        @NotNull(message = "quantity is required") @Positive(message = "quantity must be positive") Integer quantity) {
}
