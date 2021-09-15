package com.claudiodornelles.desafio;

import com.claudiodornelles.desafio.models.Salesman;
import com.claudiodornelles.desafio.service.FileInterpreterService;
import com.claudiodornelles.desafio.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    
    static DataService dataService;
    static FileInterpreterService fileInterpreterService;
    
    @Autowired
    public Main(DataService dataService,
                FileInterpreterService fileInterpreterService) {
        Main.dataService = dataService;
        Main.fileInterpreterService = fileInterpreterService;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        
        dataService.saveData(fileInterpreterService.readInputData());
        int lastInputCustomersAmount = dataService.getLastInputCustomersAmount();
        int lastInputSalesmenAmount = dataService.getLastInputSalesmanAmount();
        Long mostExpensiveSaleId = dataService.getMostExpensiveSaleId();
        Salesman worstSalesmanEver = dataService.getWorstSalesman();
        
        System.out.println("The total amount of customers imported was: " + lastInputCustomersAmount);
        System.out.println("The total amount of salesmen imported was: " + lastInputSalesmenAmount);
        System.out.println("The most expensive sale ID is:" + mostExpensiveSaleId);
        System.out.println("The worst salesman ever is: " + worstSalesmanEver.toString());
    }
}
