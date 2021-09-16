package com.claudiodornelles.desafio.builders;

import com.claudiodornelles.desafio.models.Customer;

public class CustomerBuilder {
    
    private final Customer customer;
    
    public CustomerBuilder() {
        this.customer = new Customer();
    }
    
    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }
    
    public CustomerBuilder withCpnj(String cnpj) {
        this.customer.setCnpj(cnpj);
        return this;
    }
    
    public CustomerBuilder withName(String name) {
        this.customer.setName(name);
        return this;
    }
    
    public CustomerBuilder withBusinessArea(String businessArea) {
        this.customer.setBusinessArea(businessArea);
        return this;
    }
    
    public Customer build() {
        return this.customer;
    }
    
}
