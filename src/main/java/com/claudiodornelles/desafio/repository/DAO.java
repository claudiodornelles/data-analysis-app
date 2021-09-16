package com.claudiodornelles.desafio.repository;

import com.claudiodornelles.desafio.builders.CustomerBuilder;
import com.claudiodornelles.desafio.builders.SaleBuilder;
import com.claudiodornelles.desafio.builders.SalesmanBuilder;
import com.claudiodornelles.desafio.models.Customer;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.models.Salesman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class DAO {
    
    private final SaleRepository saleRepository;
    private final SalesmanRepository salesmanRepository;
    private final CustomerRepository customerRepository;
    
    @Autowired
    public DAO(SaleRepository saleRepository, SalesmanRepository salesmanRepository, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.salesmanRepository = salesmanRepository;
        this.customerRepository = customerRepository;
    }
    
    private static final String LIST_DELIMITER = ",";
    private static final String PRODUCTS_INFO_DELIMITER = "-";
    private static final String GENERAL_DELIMITER = "ç";
    private static final String SALESMAN_PREFIX = "001" + GENERAL_DELIMITER;
    private static final String CUSTOMER_PREFIX = "002" + GENERAL_DELIMITER;
    private static final String SALE_PREFIX = "003" + GENERAL_DELIMITER;
    
    public void saveInputData(List<String> inputData) {
        for (String block : inputData) {
            List<String> info = List.of(block.split(GENERAL_DELIMITER));
            if (block.startsWith(SALESMAN_PREFIX)) {
                saveSalesmanInfo(info);
            } else if (block.startsWith(CUSTOMER_PREFIX)) {
                saveCustomerInfo(info);
            } else if (block.startsWith(SALE_PREFIX)) {
                saveSaleInfo(info);
            }
        }
    }
    
    @Async
    public void saveSalesmanInfo(List<String> info) {
        Salesman salesman = SalesmanBuilder.builder()
                                           .withCpf(info.get(1))
                                           .withName(info.get(2))
                                           .withSalary(BigDecimal.valueOf(Float.parseFloat(info.get(3))))
                                           .build();
        try {
            salesmanRepository.save(salesman);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void updateSalesmanSaleInfo(String id, BigDecimal salePrice) {
        Optional<Salesman> salesman = salesmanRepository.findById(id);
        if (salesman.isPresent()) {
            Salesman foundSalesman = salesman.get();
            foundSalesman.updateAmountSold(salePrice);
            try {
                salesmanRepository.save(foundSalesman);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    @Async
    public void saveCustomerInfo(List<String> info) {
        Customer customer = CustomerBuilder.builder()
                                           .withCpnj(info.get(1))
                                           .withName(info.get(2))
                                           .withBusinessArea(info.get(3))
                                           .build();
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Async
    public void saveSaleInfo(List<String> info) {
        BigDecimal salePrice = getSalePrice(info);
        Sale sale = SaleBuilder.builder()
                               .withId(Long.parseLong(info.get(1)))
                               .withProductList(info.get(2))
                               .withSalesman(info.get(3)).withPrice(salePrice)
                               .build();
        try {
            saleRepository.save(sale);
            updateSalesmanSaleInfo(sale.getSalesman(), salePrice);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private BigDecimal getSalePrice(List<String> saleInfo) {
        List<String> productsData = List.of(saleInfo.get(2)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(LIST_DELIMITER));
        BigDecimal salePrice = BigDecimal.ZERO;
        for (String info : productsData) {
            List<String> productInfo = List.of(info.split(PRODUCTS_INFO_DELIMITER));
            BigDecimal quantity = BigDecimal.valueOf(Float.parseFloat(productInfo.get(1)));
            BigDecimal price = BigDecimal.valueOf(Float.parseFloat(productInfo.get(2)));
            salePrice = salePrice.add(price.multiply(quantity));
        }
        return salePrice;
    }
    
    public int getCustomersAmount() {
        return customerRepository.findAll().size();
    }
    
    public int getSalesmenAmount() {
        return salesmanRepository.findAll().size();
    }
    
    public Long getMostExpensiveSaleId() {
        List<Sale> sales = saleRepository.findAll(Sort.by("price").descending());
        return sales.get(0).getId();
    }
    
    public Salesman getWorstSalesman() {
        List<Salesman> salesmen = salesmanRepository.findAll(Sort.by("amountSold").ascending());
        return salesmen.get(0);
    }
}
