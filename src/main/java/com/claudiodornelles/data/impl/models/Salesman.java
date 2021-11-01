package com.claudiodornelles.data.impl.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Salesman {
    
    private String name;
    private String cpf;
    private BigDecimal salary;
    private BigDecimal amountSold;
    
    public Salesman() {
        this.amountSold = BigDecimal.ZERO;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("A name must be passed");
        else this.name = name;
    }
    
    public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) throw new IllegalArgumentException("A cpf must be passed");
        else this.cpf = cpf;
    }
    
    public void setSalary(BigDecimal salary) {
        if (salary == null) throw new IllegalArgumentException("A salary must be passed");
        else if (salary.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Salary must be greater than zero");
        else this.salary = salary;
    }
    
    public void setAmountSold(BigDecimal amountSold) {
        if (amountSold == null) throw new IllegalArgumentException("An amount sold must be passed");
        else if (amountSold.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount sold cannot be negative");
        else this.amountSold = amountSold;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public BigDecimal getAmountSold() {
        return amountSold;
    }

    @Override
    public String toString() {
        return "{" +
               "\"name\":'" + name + '\'' +
               ", \"cpf\":'" + cpf + '\'' +
               ", \"salary\" :" + salary +
               ", \"amountSold\":" + amountSold +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salesman salesman = (Salesman) o;
        return name.equals(salesman.name) && cpf.equals(salesman.cpf) && salary.equals(salesman.salary) && amountSold.equals(salesman.amountSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cpf, salary, amountSold);
    }
}
