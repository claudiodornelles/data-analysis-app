package com.claudiodornelles.desafio.models;

import java.math.BigDecimal;

public class Salesman {
    
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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
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
}
