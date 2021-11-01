package com.claudiodornelles.data.impl.builders;

import com.claudiodornelles.data.impl.models.Salesman;

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
        this.salesman.setCpf(cpf);
        return this;
    }
    
    public SalesmanBuilder withName(String name) {
        this.salesman.setName(name);
        return this;
    }
    
    public SalesmanBuilder withSalary(BigDecimal salary) {
        this.salesman.setSalary(salary);
        return this;
    }
    
    public SalesmanBuilder withAmountSold(BigDecimal amountSold) {
        this.salesman.setAmountSold(amountSold);
        return this;
    }
    
    public Salesman build() {
        if (this.salesman.getName() == null)
            throw new IllegalArgumentException("Cannot create a salesman without a name");
        else if (this.salesman.getCpf() == null)
            throw new IllegalArgumentException("Cannot create a salesman without a CPF");
        else if (this.salesman.getSalary() == null)
            throw new IllegalArgumentException("Cannot create a salesman without a Salary");
        else return this.salesman;
    }
}
