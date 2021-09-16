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
        this.price = totalAmount;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSalesman() {
        return salesman;
    }
    
    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
