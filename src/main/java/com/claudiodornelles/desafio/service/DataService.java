package com.claudiodornelles.desafio.service;

import com.claudiodornelles.desafio.models.Customer;
import com.claudiodornelles.desafio.models.Product;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.models.Salesman;
import com.claudiodornelles.desafio.repository.CustomerRepository;
import com.claudiodornelles.desafio.repository.ProductRepository;
import com.claudiodornelles.desafio.repository.SaleRepository;
import com.claudiodornelles.desafio.repository.SalesmanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {
    
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SalesmanRepository salesmanRepository;
    private final CustomerRepository customerRepository;
    
    private int lastInputCustomersAmount = 0;
    private int lastInputSalesmanAmount = 0;
    private static final String GENERAL_DELIMITER = "ç";
    private static final String LIST_DELIMITER = ",";
    private static final String PRODUCTS_INFO_DELIMITER = "-";
    
    @Autowired
    public DataService(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       SalesmanRepository salesmanRepository,
                       CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.salesmanRepository = salesmanRepository;
        this.customerRepository = customerRepository;
    }
    
    public void saveData(List<String> fileData) {
        lastInputCustomersAmount = 0;
        lastInputSalesmanAmount = 0;
        for (String info : fileData) {
            List<String> tempData = List.of(info.split(GENERAL_DELIMITER));
            if (info.startsWith("001ç")) {
                saveSalesmanInfo(tempData);
                lastInputSalesmanAmount += 1;
            } else if (info.startsWith("002ç")) {
                saveCustomerInfo(tempData);
                lastInputCustomersAmount += 1;
            } else if (info.startsWith("003ç")) {
                saveSaleInfo(tempData);
            }
        }
    }
    
    private void saveSalesmanInfo(List<String> info) {
        Salesman salesman = new Salesman();
        salesman.setCpf(info.get(1));
        salesman.setName(info.get(2));
        salesman.setSalary(BigDecimal.valueOf(Float.parseFloat(info.get(3))));
        salesmanRepository.save(salesman);
    }
    
    private void updateSalesmanSaleInfo(String id, BigDecimal salePrice) {
        Optional<Salesman> salesman = salesmanRepository.findById(id);
        if (salesman.isPresent()) {
            Salesman foundSalesman = salesman.get();
            foundSalesman.updateAmountSold(salePrice);
            salesmanRepository.save(foundSalesman);
        }
    }
    
    private void saveCustomerInfo(List<String> info) {
        Customer customer = new Customer();
        customer.setCnpj(info.get(1));
        customer.setName(info.get(2));
        customer.setBusinessArea(info.get(3));
        customerRepository.save(customer);
    }
    
    private void saveSaleInfo(List<String> info) {
        BigDecimal salePrice = saveProductInfo(info);
        
        Sale sale = new Sale();
        sale.setId(Long.valueOf(info.get(1)));
        sale.setProductList(info.get(2));
        sale.setPrice(salePrice);
        sale.setSalesman(info.get(3));
        saleRepository.save(sale);
        updateSalesmanSaleInfo(sale.getSalesman(), salePrice);
    }
    
    private BigDecimal saveProductInfo(List<String> saleData) {
        List<String> productsData = List.of(saleData.get(2)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(LIST_DELIMITER));
        BigDecimal salePrice = BigDecimal.ZERO;
        for (String productInfo : productsData) {
            List<String> tempData = List.of(productInfo.split(PRODUCTS_INFO_DELIMITER));
            Product product = new Product();
            product.setId(Long.parseLong(tempData.get(0)));
            product.setQuantity(Float.parseFloat(tempData.get(1)));
            product.setPrice(BigDecimal.valueOf(Float.parseFloat(tempData.get(2))));
            productRepository.save(product);
            salePrice = salePrice.add(product.getPrice());
        }
        return salePrice;
    }
    
    public int getLastInputCustomersAmount() {
        return lastInputCustomersAmount;
    }
    
    public int getLastInputSalesmanAmount() {
        return lastInputSalesmanAmount;
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
