package com.kata.books.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kata.books.application.BasketPricer;
import com.kata.books.application.OptimalBasketPricer;

@Configuration
public class PricingConfiguration {

    @Bean
    BasketPricer basketPricer() {
        return new OptimalBasketPricer();
    }
}
