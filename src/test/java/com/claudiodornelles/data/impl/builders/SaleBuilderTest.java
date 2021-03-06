package com.claudiodornelles.data.impl.builders;

import com.claudiodornelles.data.impl.models.Sale;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaleBuilderTest {
    
    private Sale dummySale() {
        Sale dummySale = new Sale();
        dummySale.setId(1L);
        dummySale.setPrice(BigDecimal.valueOf(10));
        dummySale.setSalesman("Dummy Salesman");
        return dummySale;
    }
    
    private Sale buildDummySale() {
        return SaleBuilder.builder()
                          .withId(1L)
                          .withPrice(BigDecimal.valueOf(10))
                          .withSalesman("Dummy Salesman")
                          .build();
    }
    
    @Test
    void shouldBeAbleToBuildASale() {
        assertAll(
                () -> assertDoesNotThrow(this::buildDummySale),
                () -> assertEquals(dummySale(), buildDummySale())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithoutAnId() {
        IllegalArgumentException buildWithoutIdException = assertThrows(IllegalArgumentException.class,
                                                                        () -> SaleBuilder.builder()
                                                                                         .withSalesman("Dummy salesman")
                                                                                         .withPrice(new BigDecimal(10))
                                                                                         .build());
        
        assertAll(
                () -> assertEquals("Cannot create a Sale without an id", buildWithoutIdException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildWithoutIdException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithoutASalesman() {
        IllegalArgumentException buildWithoutSalesmanException = assertThrows(IllegalArgumentException.class,
                                                                              () -> SaleBuilder.builder()
                                                                                               .withId(1L)
                                                                                               .withPrice(new BigDecimal(10))
                                                                                               .build());
        
        assertAll(
                () -> assertEquals("Cannot create a Sale without a related salesman", buildWithoutSalesmanException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildWithoutSalesmanException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithEmptySalesman() {
        IllegalArgumentException buildWithEmptySalesmanException = assertThrows(IllegalArgumentException.class,
                                                                                () -> SaleBuilder.builder()
                                                                                                 .withId(1L)
                                                                                                 .withSalesman(" ")
                                                                                                 .withPrice(new BigDecimal(10))
                                                                                                 .build());
        assertAll(
                () -> assertEquals("A salesman name must be passed", buildWithEmptySalesmanException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildWithEmptySalesmanException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithNullSalesman() {
        IllegalArgumentException buildWithNullSalesmanException = assertThrows(IllegalArgumentException.class,
                                                                               () -> SaleBuilder.builder()
                                                                                                .withId(1L)
                                                                                                .withSalesman(null)
                                                                                                .withPrice(new BigDecimal(10))
                                                                                                .build());
        assertAll(
                () -> assertEquals("A salesman name must be passed", buildWithNullSalesmanException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildWithNullSalesmanException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithoutAPrice() {
        IllegalArgumentException buildSaleWithoutPriceException = assertThrows(IllegalArgumentException.class,
                                                                               () -> SaleBuilder.builder()
                                                                                                .withId(1L)
                                                                                                .withSalesman("Dummy Salesman")
                                                                                                .build());
        assertAll(
                () -> assertEquals("Cannot create a Sale without a sale price", buildSaleWithoutPriceException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSaleWithoutPriceException.getClass())
        
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithNullPrice() {
        IllegalArgumentException buildSaleWithNullPriceException = assertThrows(IllegalArgumentException.class,
                                                                                () -> SaleBuilder.builder()
                                                                                                 .withId(1L)
                                                                                                 .withPrice(null)
                                                                                                 .withSalesman("Dummy Salesman")
                                                                                                 .build());
        assertAll(
                () -> assertEquals("A total amount must be passed", buildSaleWithNullPriceException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSaleWithNullPriceException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithInvalidId() {
        IllegalArgumentException buildSaleWithZeroIdException = assertThrows(IllegalArgumentException.class,
                                                                             () -> SaleBuilder.builder()
                                                                                              .withId(0L)
                                                                                              .withPrice(new BigDecimal(10))
                                                                                              .build());
        assertAll(
                () -> assertEquals("The id must be a number greater than zero", buildSaleWithZeroIdException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSaleWithZeroIdException.getClass())
        
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithNegativeId() {
        IllegalArgumentException buildSaleWithNegativeIdException = assertThrows(IllegalArgumentException.class,
                                                                                 () -> SaleBuilder.builder()
                                                                                                  .withId(-10L)
                                                                                                  .withPrice(new BigDecimal(10))
                                                                                                  .build());
        assertAll(
                () -> assertEquals("The id must be a number greater than zero", buildSaleWithNegativeIdException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSaleWithNegativeIdException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASaleWithInvalidPrice() {
        IllegalArgumentException buildSaleWithNegativePriceException = assertThrows(IllegalArgumentException.class,
                                                                                    () -> SaleBuilder.builder()
                                                                                                     .withId(1L)
                                                                                                     .withPrice(new BigDecimal(-100))
                                                                                                     .withSalesman("Dummy Salesman")
                                                                                                     .build());
        assertAll(
                () -> assertEquals("Total amount must be greater than zero", buildSaleWithNegativePriceException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSaleWithNegativePriceException.getClass())
                 );
    }
}