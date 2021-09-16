package com.claudiodornelles.desafio.builders;

import com.claudiodornelles.desafio.models.Salesman;

import java.math.BigDecimal;

public class SalesmanBuilder {
    
    private final Salesman salesman;
    
    public SalesmanBuilder() {
        this.salesman = new Salesman();
    }
    
    public static SalesmanBuilder builder() {
        return new SalesmanBuilder();
    }
    
    public SalesmanBuilder withCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) throw new IllegalArgumentException("A cpf must be passed");
        else {
            this.salesman.setCpf(cpf);
            return this;
        }
    }
    
    public SalesmanBuilder withName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("A name must be passed");
        else {
            this.salesman.setName(name);
            return this;
        }
    }
    
    public SalesmanBuilder withSalary(BigDecimal salary) {
        if (salary == null) throw new IllegalArgumentException("A salary must be passed");
        else if (salary.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Salary must be greater than zero");
        else {
            this.salesman.setSalary(salary);
            return this;
        }
    }
    
    public SalesmanBuilder withAmountSold(BigDecimal amountSold) {
        if (amountSold == null) throw new IllegalArgumentException("An amount sold must be passed");
        else if (amountSold.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount sold cannot be negative");
        else {
            this.salesman.setAmountSold(amountSold);
            return this;
        }
    }
    
    public Salesman build() {
        if (this.salesman.getName() == null) throw new IllegalArgumentException("Cannot create a salesman without a name");
        else if (this.salesman.getCpf() == null) throw new IllegalArgumentException("Cannot create a salesman without a CPF");
        else if (this.salesman.getSalary() == null) throw new IllegalArgumentException("Cannot create a salesman without a Salary");
        else if (this.salesman.getAmountSold() == null) throw new IllegalArgumentException("Cannot create a salesman without an amount sold information");
        else return this.salesman;
    }
}
