package com.claudiodornelles.desafio.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    
    @Id
    private long id;
    private float quantity;
    private BigDecimal price;
    
    public float getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public float getQuantity() {
        return quantity;
    }
    
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
