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
    
    public Salesman build() {
        return this.salesman;
    }
}
