package com.kata.books.infrastructure.web;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record BasketPriceRequest(@NotEmpty(message = "items must not be empty") List<@Valid BasketItemRequest> items) {

}
