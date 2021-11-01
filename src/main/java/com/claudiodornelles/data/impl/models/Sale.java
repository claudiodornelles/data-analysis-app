package com.claudiodornelles.data.impl.models;

import java.math.BigDecimal;
import java.util.Objects;

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
        if (salesman == null || salesman.trim().isEmpty())
            throw new IllegalArgumentException("A salesman name must be passed");
        else this.salesman = salesman;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return id == sale.id && salesman.equals(sale.salesman) && price.equals(sale.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, salesman, price);
    }

    @Override
    public String toString() {
        return "Sale{" +
               "id=" + id +
               ", salesman='" + salesman + '\'' +
               ", price=" + price +
               '}';
    }
}
