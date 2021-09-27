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
        if (this.sale.getId() == 0) throw new IllegalArgumentException("Cannot create a Sale without an id");
        else if (this.sale.getSalesman() == null)
            throw new IllegalArgumentException("Cannot create a Sale without a related salesman");
        else if (this.sale.getPrice() == null)
            throw new IllegalArgumentException("Cannot create a Sale without a sale price");
        else return this.sale;
    }
}
