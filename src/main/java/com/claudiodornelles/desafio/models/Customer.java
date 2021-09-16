package com.claudiodornelles.desafio.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "customers")
public class Customer {
    
    @Id
    private String cnpj;
    private String name;
    private String businessArea;
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBusinessArea() {
        return businessArea;
    }
    
    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
}
