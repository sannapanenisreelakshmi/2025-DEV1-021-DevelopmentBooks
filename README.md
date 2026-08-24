Software Development Books:

This project is a Spring Boot application developed using Java and Maven.

The purpose of this project is to demonstrate clean, maintainable, and production-ready Java backend development, TDD practices.

🛠️ Technologies

Java 21

Spring Boot

Maven

JUnit 

Git & GitHub

📁 Project Structure

src

├── main

│   ├── java

│   │   └── com.kata.books

│   │       └── KataApplication.java

│   └── resources

│       └── application.yaml

│

└── test

   └── java
   
   

🚀 Getting Started
Prerequisites
Make sure the following are installed:
Java 21 or later 
Maven
Git

Check the installed versions:

java -version
mvn -version
git --version
Clone the Repository
git clone <https://github.com/sannapanenisreelakshmi/2025-DEV1-021-DevelopmentBooks.git>

Navigate to the project:
cd <2025-DEV1-021-DevelopmentBooks>

Build the Project

mvn clean install

Run the Application

mvn spring-boot:run
The application will start on:
http://localhost:8092

Update2:

BookBasket

BookBasket is a Java record that represents a collection of books and the quantity requested for each book.

The implementation is designed with a focus on immutability, input validation, defensive copying, and clean object creation.

Overview

A BookBasket maintains book quantities using:

Map<BookTitle, Integer>

where:

BookTitle represents the book.
Integer represents the requested quantity.

For example:

CLEAN_CODE      → 2
EFFECTIVE_JAVA  → 1

Key Features
1. Java Record

BookBasket is implemented as a Java record. Records are well suited for data-carrying objects and automatically provide:

Accessor method: quantities(), equals(), hashCode(), toString()

This keeps the class concise while clearly expressing its purpose.

2. Input Validation
The constructor validates all incoming data before creating the basket.
Quantities map cannot be null
Passing a null map results in a NullPointerException.
Book title cannot be null.Each book title is validated.This ensures that every entry has a valid BookTitle and quantity must be positive

The basket only accepts positive quantities.Therefore, the following values are invalid:

null
0
-1
-2
...

Valid quantities are:

1
2
3
...

Invalid input is rejected immediately, following a fail-fast approach.

3. Defensive Copying

The constructor creates a new EnumMap:

var copiedQuantities =
        new EnumMap<BookTitle, Integer>(BookTitle.class);

The validated entries from the input map are copied into this new map.

This prevents the BookBasket from directly depending on the caller's mutable map.

Example
var quantities =
        new EnumMap<BookTitle, Integer>(BookTitle.class);

quantities.put(BookTitle.CLEAN_CODE, 2);

var basket = new BookBasket(quantities);

If the caller later modifies the original map:

quantities.put(BookTitle.CLEAN_CODE, 10);

the basket does not use the original map as its internal storage.

This is an important defensive programming technique.

4. Why EnumMap?

EnumMap is used because BookTitle is an enum. EnumMap is specifically designed for enum keys and is an appropriate choice when the map key is an enum.

5. Immutable Internal State

After validation and copying, the map is converted using:

quantities = Map.copyOf(copiedQuantities);

Map.copyOf() returns an unmodifiable map.

This prevents the internal collection from being modified after the BookBasket has been created.

The overall process is:

Input Map
    ↓
Validate
    ↓
Copy into EnumMap
    ↓
Map.copyOf()
    ↓
Unmodifiable Map
    ↓
BookBasket

This provides a strong separation between the caller's mutable input and the basket's internal state.

6. Empty Basket Factory Method

The class provides a convenient factory method:

public static BookBasket empty() {
    return new BookBasket(Map.of());
}

Instead of writing:

new BookBasket(Map.of());

you can simply write:

BookBasket.empty();

This makes the intention of the code clearer.

Example
var basket = BookBasket.empty();

This creates a valid basket containing no books.

7. Example Usage
var quantities =
        new EnumMap<BookTitle, Integer>(BookTitle.class);

quantities.put(BookTitle.CLEAN_CODE, 2);
quantities.put(BookTitle.EFFECTIVE_JAVA, 1);

var basket = new BookBasket(quantities);

The quantities can be accessed using the record accessor:
basket.quantities();

8. Validation Rules
Input	Behaviour
null quantities map	NullPointerException
null book title	NullPointerException
null quantity	IllegalArgumentException
Quantity 0	IllegalArgumentException
Negative quantity	IllegalArgumentException
Positive quantity	Accepted
Empty map	Accepted

9. Design Principles: This implementation demonstrates several important Java and software design principles.

Fail Fast: Invalid data is rejected during object creation rather than allowing an invalid BookBasket to exist.

Defensive Copying: The input map is copied instead of being stored directly.

Immutability

Map.copyOf() ensures that the stored map cannot be modified through the map API.

Encapsulation: The internal representation is protected from external modification.

Single Responsibility : The BookBasket is responsible for representing and validating the state of a basket.

Factory Method

BookBasket.empty() provides a readable way to create an empty basket.

10. Why Use var?

The implementation uses:

var copiedQuantities =
        new EnumMap<BookTitle, Integer>(BookTitle.class);

var allows Java to infer the local variable's type at compile time.

The compiler understands this as:

EnumMap<BookTitle, Integer> copiedQuantities =
        new EnumMap<>(BookTitle.class);

var does not mean that Java becomes dynamically typed. The variable still has a fixed compile-time type.

11. Design Flow

The complete object creation flow can be summarized as:

             Caller
                |
                v
       Map<BookTitle, Integer>
                |
                v
        Validate input
          /          \
       Invalid       Valid
         |             |
         v             v
     Exception     Defensive Copy
                       |
                       v
                   EnumMap
                       |
                       v
                  Map.copyOf()
                       |
                       v
                BookBasket
12. Summary

BookBasket is a small but well-structured Java component that demonstrates how to create a reliable value object using modern Java features.

The implementation follows the principle:

Validate → Copy → Protect

Specifically, it:

Validates that the input map is not null.
Validates every BookTitle.
Ensures every quantity is positive.
Creates a defensive copy using EnumMap.
Converts the copy into an unmodifiable map using Map.copyOf().
Uses a Java record to reduce boilerplate.
Provides BookBasket.empty() for convenient empty-basket creation.

This design helps ensure that a BookBasket is created in a valid state and that its internal collection is protected from unexpected external modification.

Update 3 :

Money

Money is a Java record used to represent monetary amounts with a currency.

Features
Uses BigDecimal for accurate monetary calculations.
Currently supports EUR only.
Rejects null amount and currency.
Rejects unsupported currencies.
Rounds amounts to 2 decimal places using RoundingMode.HALF_UP.
Provides convenient eur() factory methods.
Example
Money price = Money.eur("10.125");

The amount is automatically rounded:

10.125 → 10.13 EUR

You can also create it using BigDecimal:

Money price = Money.eur(new BigDecimal("19.99"));
Validation
Input	Result
Money.eur("10")	10.00 EUR
Money.eur("10.125")	10.13 EUR
null amount	NullPointerException
null currency	NullPointerException
Non-EUR currency	IllegalArgumentException
Design

The object follows this flow:

Validate → Check EUR → Round to 2 decimals → Create Money

Using BigDecimal and centralized validation makes Money a reliable value object for monetary operations.

Distinct Book Set Discounts

This test verifies that the correct discount is applied when a basket contains different book titles.

Discount Rules
Distinct-Books	Discount	Expected Price
1	0%	€50.00
2	5%	€95.00
3	10%	€135.00
4	20%	€160.00
5	25%	€187.50

Test
@Test
void appliesEveryDistinctSetDiscount() {

    assertThat(price(1, 0, 0, 0, 0))
        .isEqualTo(Money.eur("50.00"));

    assertThat(price(1, 1, 0, 0, 0))
        .isEqualTo(Money.eur("95.00"));

    assertThat(price(1, 1, 1, 0, 0))
        .isEqualTo(Money.eur("135.00"));

    assertThat(price(1, 1, 1, 1, 0))
        .isEqualTo(Money.eur("160.00"));

    assertThat(price(1, 1, 1, 1, 1))
        .isEqualTo(Money.eur("187.50"));
}

The test ensures that the pricing logic correctly handles 1 to 5 distinct books and applies the corresponding discount.

update4 :

Repeated Discounted Sets

This test verifies that discounts are correctly applied when the basket contains multiple sets of distinct books.

Test Scenarios
Book Quantities	Sets	Expected Price
2, 2, 0, 0, 0	2 sets of 2 books	€190.00
3, 3, 3, 3, 3	3 sets of 5 books	€562.50

The test ensures that the pricing logic: Identifies separate sets of distinct books. Applies the appropriate discount to each set. Adds the discounted prices to calculate the final amount.

Key concept: Discounts are calculated per distinct set, not simply based on the total number of books.

update 5 :

Optimal Discount Set Selection

This test verifies that the pricing logic chooses the most cost-effective combination of discounted sets.

Test Scenario
@Test
void choosesTwoFourTitleSetsInsteadOfAFiveAndAThreeTitleSet() {
    assertThat(price(2, 2, 2, 1, 1))
        .isEqualTo(Money.eur("320.00"));
}

For the quantities:

2, 2, 2, 1, 1

the algorithm can create either:

5-title + 3-title sets → €322.50
Two 4-title sets → €320.00 ✅

The test ensures that the cheapest combination (€320.00) is selected.

Purpose

The pricing algorithm should choose the optimal combination of discounted sets rather than simply creating the largest possible set.

update: 6

Order-Independent Pricing

This test verifies that the Pricer produces the same price regardless of the order of book entries in the BookBasket.

Test Scenario
@Test
void producesTheSamePriceRegardlessOfEntryOrder() {
    var first = new BookBasket(Map.of(
        BookTitle.CLEAN_CODE, 2,
        BookTitle.CLEAN_CODER, 1,
        BookTitle.CLEAN_ARCHITECTURE, 2
    ));


    var second = new BookBasket(Map.of(
        BookTitle.CLEAN_ARCHITECTURE, 2,
        BookTitle.CLEAN_CODE, 2,
        BookTitle.CLEAN_CODER, 1
    ));


    assertThat(pricer.price(first))
        .isEqualTo(pricer.price(second));
}
Purpose : The test ensures:

Same books and quantities → same price
Different entry order → no change in price
Pricing logic is order-independent and deterministic.

update 7:
Undiscounted Basket Pricer

UndiscountedBasketPricer calculates the basket price using a fixed €50.00 unit price per book.

How It Works
Total books × €50.00 = Basket price
Example
4 books × €50.00 = €200.00

The result is returned as a Money value in EUR.

Note: This pricer does not apply any discounts.

update 8 :

Single Book Pricing

This test verifies that one book costs €50.00.

Test Scenario
@Test
void pricesOneBookAtFiftyEuros() {
    var basket = new BookBasket(
        Map.of(BookTitle.CLEAN_CODE, 1)
    );


    assertThat(pricer.price(basket))
        .isEqualTo(Money.eur("50.00"));
}
Expected Result
1 book × €50.00 = €50.00

Purpose: Ensures the basic pricing logic correctly calculates the price of a single book.

Update 9 :

Multiple Copies of One Title

This test verifies that multiple copies of the same book title receive no discount.

Test Scenario
@Test
void appliesNoDiscountToMultipleCopiesOfOneTitle() {
    var basket = new BookBasket(
        Map.of(BookTitle.CLEAN_CODE, 3)
    );


    assertThat(pricer.price(basket))
        .isEqualTo(Money.eur(new BigDecimal("150.00")));
}
Expected Result
3 books × €50.00 = €150.00

Purpose: Ensures discounts are applied only when there are different book titles, not simply multiple copies of the same title.

update 10:

Basket Pricing API Integration Tests

Integration tests for the /api/v1/baskets/price REST API using Spring Boot, MockMvc, and JUnit 5.

Covered Scenarios
✅ Single and multi-title basket pricing
✅ Optimal discount grouping
✅ Unknown book title validation
✅ Zero/negative quantity validation
✅ Missing quantity validation
✅ Empty and malformed requests
✅ HTTP status and JSON response validation
Test Setup
@SpringBootTest
@AutoConfigureMockMvc
class BasketPricingApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
}

These tests verify the API end-to-end from HTTP request to pricing response, including validation and error handling.

Update : 11

Basket Pricing API Integration Tests

This test class verifies the basket pricing REST API using Spring Boot, MockMvc, and JUnit 5.

Covered Scenarios
✅ Prices single and multiple book titles → €95.00
✅ Selects the optimal discount grouping → €320.00
✅ Rejects unknown book titles
✅ Rejects zero or negative quantities
✅ Rejects missing quantities
✅ Rejects empty requests
✅ Rejects malformed JSON
✅ Verifies HTTP status codes and JSON error responses
Test Flow
HTTP POST Request
       ↓
/api/v1/baskets/price
       ↓
Controller & Validation
       ↓
BasketPricer
       ↓
JSON Response

Purpose: Ensures the basket pricing API works correctly for both valid pricing scenarios and invalid requests, including validation and error handling.


Update : 12

Basket Pricing Controller

BasketPricingController exposes the REST API for calculating a book basket price.

Endpoint
POST /price
How It Works
HTTP Request
    ↓
BasketPriceRequest
    ↓
Convert items to book quantities
    ↓
BasketPricer
    ↓
BookBasket
    ↓
BasketPriceResponse
Key Responsibilities
Accepts basket pricing requests using @RequestBody.
Validates the request using @Valid.
Converts request items into Map<BookTitle, Integer>.
Uses BasketPricer to calculate the price.
Returns the currency and total amount in the response.
Uses Math.addExact() when combining quantities to safely detect integer overflow.
Example Response
{
  "currency": "EUR",
  "total": 95.00
}

Purpose: Keeps the controller focused on HTTP request/response handling, while the actual pricing logic remains inside BasketPricer.

Update 13:

The service listens on `http://localhost:8092` .

## API
  http://localhost:8092/api/v1/baskets/price

### Price a basket

`POST /api/v1/baskets/price`

```json
{
  "items": [
    { "title": "CLEAN_CODE", "quantity": 1 },
    { "title": "CLEAN_CODER", "quantity": 1 }
  ]
}
```

Successful response (`200 OK`):

```json
{
  "currency": "EUR",
  "total": 95.00
}
```

Requests with malformed JSON, unknown titles, or invalid body structure return `400 Bad Request`:

```json
{
  "code": "INVALID_REQUEST",
  "message": "Request body is invalid"
}
```

Missing, empty, zero, or negative quantities return a validation error:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "quantity quantity must be positive"
}
```

calculating cost for this book basket 

2 copies of the “Clean Code” book
2 copies of the “Clean Coder” book
2 copies of the “Clean Architecture” book
1 copy of the “Test Driven Development by Example” book
1 copy of the “Working effectively with Legacy Code” book

```json
{
  "items": [
    { "title": "CLEAN_CODE", "quantity": 2 },
    { "title": "CLEAN_CODER", "quantity": 2 },
	{ "title": "CLEAN_ARCHITECTURE", "quantity": 2 },
	{ "title": "TDD_BY_EXAMPLE", "quantity": 1 },
	{ "title": "WORKING_EFFECTIVELY_WITH_LEGACY_CODE", "quantity": 1 }
  ]
}
```
Result :  
{
    "currency": "EUR",
    "total": 320.00
}

