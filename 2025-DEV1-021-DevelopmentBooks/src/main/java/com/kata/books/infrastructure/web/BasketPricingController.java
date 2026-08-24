package com.kata.books.infrastructure.web;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kata.books.application.BasketPricer;
import com.kata.books.domain.BookBasket;
import com.kata.books.domain.BookTitle;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/baskets")
public class BasketPricingController {

    private final BasketPricer basketPricer;

    public BasketPricingController(BasketPricer basketPricer) {
        this.basketPricer = basketPricer;
    }

    @PostMapping("/price")
    @ResponseStatus(HttpStatus.OK)
    public BasketPriceResponse price(@Valid @RequestBody BasketPriceRequest request) {
        var price = basketPricer.price(new BookBasket(toQuantities(request)));
        return new BasketPriceResponse(price.currency().getCurrencyCode(), price.amount());
    }
    private Map<BookTitle, Integer> toQuantities(BasketPriceRequest request) {
        var quantities = new EnumMap<BookTitle, Integer>(BookTitle.class);
        request.items().forEach(item -> quantities.merge(item.title(), item.quantity(), Math::addExact));
        return quantities;
    }
}
