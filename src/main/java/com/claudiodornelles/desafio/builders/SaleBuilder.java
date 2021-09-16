package com.claudiodornelles.desafio.builders;

import com.claudiodornelles.desafio.models.Sale;

import java.math.BigDecimal;

public class SaleBuilder {
    
    private final Sale sale;
    
    public SaleBuilder() {
        this.sale = new Sale();
    }
    
    public static SaleBuilder builder() {
        return new SaleBuilder();
    }
    
    public SaleBuilder withId(long id) {
        this.sale.setId(id);
        return this;
    }
    
    public SaleBuilder withSalesman(String salesman) {
        this.sale.setSalesman(salesman);
        return this;
    }
    
    public SaleBuilder withPrice(BigDecimal salePrice) {
        this.sale.setPrice(salePrice);
        return this;
    }
    
    public Sale build() {
        return this.sale;
    }
}
