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
    
    public SaleBuilder withId(Long id) {
        if (id == null) throw new IllegalArgumentException("An id must be passed");
        else if (id <= 0) throw new IllegalArgumentException("The id must be a number greater than 0");
        else {
            this.sale.setId(id);
            return this;
        }
    }
    
    public SaleBuilder withSalesman(String salesman) {
        if (salesman == null || salesman.isBlank())
            throw new IllegalArgumentException("A salesman name must be passed");
        else {
            this.sale.setSalesman(salesman);
            return this;
        }
    }
    
    public SaleBuilder withPrice(BigDecimal salePrice) {
        if (salePrice == null) throw new IllegalArgumentException("A sale price must be passed");
        else if (salePrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Sale price cannot be negative");
        else {
            this.sale.setPrice(salePrice);
            return this;
        }
    }
    
    public Sale build() {
        if (this.sale.getId() == null) throw new IllegalArgumentException("Cannot create a Sale without an id");
        else if (this.sale.getSalesman() == null)
            throw new IllegalArgumentException("Cannot create a Sale without a related salesman");
        else if (this.sale.getPrice() == null)
            throw new IllegalArgumentException("Cannot create a Sale without a sale price");
        else return this.sale;
    }
}
