package com.claudiodornelles.desafio.models;

import java.math.BigDecimal;

public class Sale {
    
    private Long id;
    private String salesman;
    private BigDecimal price;
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal totalAmount) {
        if (totalAmount == null) throw new IllegalArgumentException("A total amount must be passed");
        else if (totalAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Total amount cannot be less than zero");
        else this.price = totalAmount;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        if (id == null) throw new IllegalArgumentException("An id must be passed");
        else if (id <= 0) throw new IllegalArgumentException("The id must be a number greater than 0");
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
