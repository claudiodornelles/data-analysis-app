package com.claudiodornelles.desafio.models;

import java.math.BigDecimal;

public class Sale {
    
    private long id;
    private String salesman;
    private BigDecimal price;
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal totalAmount) {
        if (totalAmount == null) throw new IllegalArgumentException("A total amount must be passed");
        else if (totalAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Total amount must be greater than zero");
        else this.price = totalAmount;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        if (id <= 0) throw new IllegalArgumentException("The id must be a number greater than zero");
        else this.id = id;
    }
    
    public String getSalesman() {
        return salesman;
    }
    
    public void setSalesman(String salesman) {
        if (salesman == null || salesman.isBlank())
            throw new IllegalArgumentException("A salesman name must be passed");
        else this.salesman = salesman;
    }
}
