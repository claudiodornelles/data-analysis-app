package com.claudiodornelles.desafio.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Salesman {
    
    @Id
    private String name;
    private String cpf;
    private BigDecimal salary;
    private BigDecimal amountSold;
    
    @Override
    public String toString() {
        return "{" +
               "\"name\":'" + name + '\'' +
               ", \"cpf\":'" + cpf + '\'' +
               ", \"salary\" :" + salary +
               ", \"amountSold\":" + amountSold +
               '}';
    }
    
    public Salesman() {
        this.amountSold = BigDecimal.ZERO;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public BigDecimal getAmountSold() {
        return amountSold;
    }
    
    public void setAmountSold(BigDecimal amountSold) {
        this.amountSold = amountSold;
    }
    
    public void updateAmountSold(BigDecimal amountSold) {
        this.amountSold = getAmountSold().add(amountSold);
    }
}
