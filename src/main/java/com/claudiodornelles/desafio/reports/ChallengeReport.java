package com.claudiodornelles.desafio.reports;

import com.claudiodornelles.desafio.builders.SaleBuilder;
import com.claudiodornelles.desafio.builders.SalesmanBuilder;
import com.claudiodornelles.desafio.dao.FileDAO;
import com.claudiodornelles.desafio.models.Report;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.models.Salesman;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class ChallengeReport implements Runnable, Report {
    
    private File file;
    private final String listDelimiter;
    private final String productsInfoDelimiter;
    private final String generalDelimiter;
    private final String salesmanPrefix;
    private final String customerPrefix;
    private final String salePrefix;
    private final FileDAO fileDAO;
    
    private final List<String> customersData = new ArrayList<>();
    private final List<Sale> salesData = new ArrayList<>();
    private final List<Salesman> salesmenData = new ArrayList<>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeReport.class);
    
    @Autowired
    public ChallengeReport(@Value("${list.delimiter}") String listDelimiter,
                           @Value("${products.info.delimiter}") String productsInfoDelimiter,
                           @Value("${general.delimiter}") String generalDelimiter,
                           @Value("${salesman.prefix}") String salesmanPrefix,
                           @Value("${customer.prefix}") String customerPrefix,
                           @Value("${sale.prefix}") String salePrefix,
                           FileDAO fileDAO) {
        this.listDelimiter = listDelimiter;
        this.productsInfoDelimiter = productsInfoDelimiter;
        this.generalDelimiter = generalDelimiter;
        this.salesmanPrefix = salesmanPrefix;
        this.customerPrefix = customerPrefix;
        this.salePrefix = salePrefix;
        this.fileDAO = fileDAO;
    }
    
    @Override
    public void run() {
        tailorFileData(readSource());
        writeReport();
    }
    
    @Override
    public void setSource(File file) {
        this.file = file;
    }
    
    @Override
    public List<String> readSource() {
        return fileDAO.readFile(file);
    }
    
    @Override
    public void writeReport() {
        fileDAO.writeReport(getReport(), file);
    }
    
    protected void tailorFileData(@NotNull List<String> fileData) {
        for (String element : fileData) {
            if (element.startsWith(customerPrefix)) {
                customersData.add(element);
            } else if (element.startsWith(salePrefix)) {
                salesData.add(tailorSaleData(element));
            }
        }
        for (String element : fileData) {
            if (element.startsWith(salesmanPrefix)) {
                salesmenData.add(tailorSalesmanData(element));
            }
        }
    }
    
    protected Sale tailorSaleData(String data) {
        try {
            BigDecimal salePrice = BigDecimal.ZERO;
            List<String> saleInfo = List.of(data.split(generalDelimiter));
            List<String> products = List.of(saleInfo.get(2)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(listDelimiter));
            for (String product : products) {
                List<String> productInfo = List.of(product.split(productsInfoDelimiter));
                BigDecimal quantity = new BigDecimal(productInfo.get(1));
                BigDecimal price = new BigDecimal(productInfo.get(2));
                salePrice = salePrice.add(price.multiply(quantity));
            }
            return SaleBuilder.builder()
                              .withId(Long.parseLong(saleInfo.get(1)))
                              .withSalesman(saleInfo.get(3))
                              .withPrice(salePrice)
                              .build();
        } catch (Exception e) {
            LOGGER.error("Error building a valid Sale data");
            LOGGER.debug("Error with received data from file: " + file.getName());
            LOGGER.trace(e.toString());
            return null;
        }
    }
    
    protected Salesman tailorSalesmanData(String data) {
        try {
            List<String> salesmanInfo = List.of(data.split(generalDelimiter));
            String name = salesmanInfo.get(2);
            List<Sale> sales = salesData.stream().filter(sale -> sale.getSalesman().equals(name)).collect(Collectors.toList());
            BigDecimal amountSold = BigDecimal.ZERO;
            for (Sale sale : sales) {
                amountSold = amountSold.add(sale.getPrice());
            }
            return SalesmanBuilder.builder()
                                  .withCpf(salesmanInfo.get(1))
                                  .withName(name)
                                  .withSalary(new BigDecimal(salesmanInfo.get(3)))
                                  .withAmountSold(amountSold)
                                  .build();
        } catch (Exception e) {
            LOGGER.error("Error building a valid Salesman data");
            LOGGER.debug("Error with received data from file: " + file.getName());
            LOGGER.trace(e.toString());
            return null;
        }
    }
    
    protected Long getMostExpensiveSaleId() {
        Sale mostExpansiveSale = new Sale();
        mostExpansiveSale.setPrice(BigDecimal.ZERO);
        for (Sale sale : salesData) {
            if (sale.getPrice().compareTo(mostExpansiveSale.getPrice()) > 0) {
                mostExpansiveSale = sale;
            }
        }
        if (mostExpansiveSale.getId() != null) {
            return mostExpansiveSale.getId();
        } else {
            LOGGER.debug("No sale found.");
            return null;
        }
    }
    
    protected Salesman getWorstSalesmanEver() {
        try {
            List<Salesman> tempData = salesmenData;
            tempData.sort(Comparator.comparing(Salesman::getAmountSold));
            return tempData.get(0);
        } catch (Exception e) {
            LOGGER.error("Could not execute method getWorstSalesmanEver()");
            LOGGER.trace(e.toString());
            return null;
        }
    }
    
    protected int getSalesmenAmount() {
        return salesmenData.size();
    }
    
    protected int getCustomersAmount() {
        return customersData.size();
    }
    
    protected String getReport() {
        return "The total amount of customers is: " + getCustomersAmount() + "\n" +
               "The total amount of salesmen is: " + getSalesmenAmount() + "\n" +
               "The most expensive sale has ID: " + getMostExpensiveSaleId() + "\n" +
               "The worst salesman ever is: " + getWorstSalesmanEver() + "\n";
    }
    
    protected List<String> getCustomersData() {
        return customersData;
    }
    
    protected List<Sale> getSalesData() {
        return salesData;
    }
    
    protected List<Salesman> getSalesmenData() {
        return salesmenData;
    }
}
