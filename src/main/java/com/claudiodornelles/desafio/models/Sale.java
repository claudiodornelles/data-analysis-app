package com.claudiodornelles.desafio.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Sale {
    
    @Id
    private Long id;
    private String products;
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
    
    public String getProductList() {
        return products;
    }
    
    public void setProductList(String productList) {
        this.products = productList;
    }
    
    public String getSalesman() {
        return salesman;
    }
    
    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
