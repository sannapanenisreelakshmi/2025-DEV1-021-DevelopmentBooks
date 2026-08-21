package com.kata.books.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.kata.books.domain.BookBasket;
import com.kata.books.domain.BookTitle;
import com.kata.books.domain.Money;

public final class OptimalBasketPricer implements BasketPricer {

	private static final BigDecimal UNIT_PRICE = new BigDecimal("50.00");
    private final DiscountPolicy discountPolicy;

    public OptimalBasketPricer() {
        this(new DiscountPolicy());
    }

    public OptimalBasketPricer(DiscountPolicy discountPolicy) {
        this.discountPolicy = Objects.requireNonNull(discountPolicy, "discountPolicy must not be null");
    }

    @Override
    public Money price(BookBasket basket) {
        Objects.requireNonNull(basket, "basket must not be null");
        var quantities = new ArrayList<Integer>();
        for (BookTitle title : BookTitle.values()) {
            quantities.add(basket.quantities().getOrDefault(title, 0));
        }
        return Money.eur(minimumPrice(quantities, new HashMap<>()));
    }
    private BigDecimal minimumPrice(List<Integer> remaining, Map<List<Integer>, BigDecimal> memo) {
        if (remaining.stream().allMatch(quantity -> quantity == 0)) {
            return BigDecimal.ZERO;
        }

        var state = List.copyOf(remaining);
        var cachedPrice = memo.get(state);
        if (cachedPrice != null) {
            return cachedPrice;
        }

        BigDecimal lowestPrice = null;
        for (int subset = 1; subset < (1 << BookTitle.values().length); subset++) {
            if (!isAvailable(subset, remaining)) {
                continue;
            }
            var candidatePrice = setPrice(Integer.bitCount(subset))
                    .add(minimumPrice(removeSet(subset, remaining), memo));
            if (lowestPrice == null || candidatePrice.compareTo(lowestPrice) < 0) {
                lowestPrice = candidatePrice;
            }
        }
        memo.put(state, lowestPrice);
        return lowestPrice;
    }
    
    private boolean isAvailable(int subset, List<Integer> remaining) {
        for (int index = 0; index < remaining.size(); index++) {
            if ((subset & (1 << index)) != 0 && remaining.get(index) == 0) {
                return false;
            }
        }
        return true;
    }
    private List<Integer> removeSet(int subset, List<Integer> remaining) {
        var next = new ArrayList<>(remaining);
        for (int index = 0; index < next.size(); index++) {
            if ((subset & (1 << index)) != 0) {
                next.set(index, next.get(index) - 1);
            }
        }
        return List.copyOf(next);
    }

    private BigDecimal setPrice(int distinctTitles) {
        var undiscounted = UNIT_PRICE.multiply(BigDecimal.valueOf(distinctTitles));
        return undiscounted.multiply(BigDecimal.ONE.subtract(discountPolicy.discountFor(distinctTitles)))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
