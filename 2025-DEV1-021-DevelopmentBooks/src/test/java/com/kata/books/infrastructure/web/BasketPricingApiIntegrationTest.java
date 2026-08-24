package com.kata.books.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BasketPricingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pricesASingleAndMultiTitleBasket() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"items":[
                                  {"title":"CLEAN_CODE","quantity":1},
                                  {"title":"CLEAN_CODER","quantity":1}
                                ]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.total").value(95.00));
    }
    @Test
    void selectsTheOptimalGroupingOverHttp() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"items":[
                                  {"title":"CLEAN_CODE","quantity":2},
                                  {"title":"CLEAN_CODER","quantity":2},
                                  {"title":"CLEAN_ARCHITECTURE","quantity":2},
                                  {"title":"TDD_BY_EXAMPLE","quantity":1},
                                  {"title":"WORKING_EFFECTIVELY_WITH_LEGACY_CODE","quantity":1}
                                ]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(320.00));
    }

    @Test
    void rejectsAnUnknownTitle() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + "\"items\":[{\"title\":\"UNKNOWN\",\"quantity\":1}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void rejectsZeroOrNegativeQuantities() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + "\"items\":[{\"title\":\"CLEAN_CODE\",\"quantity\":0}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsAnItemWithoutAQuantity() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + "\"items\":[{\"title\":\"CLEAN_CODE\"}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsEmptyAndMalformedRequests() throws Exception {
        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"items\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        mockMvc.perform(post("/api/v1/baskets/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }
}
