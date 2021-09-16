package com.claudiodornelles.desafio.builders;

import com.claudiodornelles.desafio.models.Sale;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaleBuilderTest {

    private Sale dummySale() {
        Sale dummySale = new Sale();
        dummySale.setId(1L);
        dummySale.setPrice(BigDecimal.valueOf(10));
        dummySale.setSalesman("Dummy Salesman");
        return dummySale;
    }
    
    private Sale builtDummySale() {
        return SaleBuilder.builder()
                          .withId(1L)
                          .withPrice(BigDecimal.valueOf(10))
                          .withSalesman("Dummy Salesman")
                          .build();
    }
    
    @Test
    void shouldBeAbleToBuildASale() {
        assertAll(
                () -> assertDoesNotThrow(this::builtDummySale),
                () -> assertEquals(dummySale().getId(), builtDummySale().getId()),
                () -> assertEquals(dummySale().getPrice(), builtDummySale().getPrice()),
                () -> assertEquals(dummySale().getSalesman(), builtDummySale().getSalesman())
                 );
    }
}