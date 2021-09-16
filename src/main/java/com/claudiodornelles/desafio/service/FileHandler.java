package com.claudiodornelles.desafio.service;

import com.claudiodornelles.desafio.builders.SaleBuilder;
import com.claudiodornelles.desafio.builders.SalesmanBuilder;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.models.Salesman;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class FileHandler implements Runnable {
    
    private File file;
    private final String listDelimiter;
    private final String productsInfoDelimiter;
    private final String generalDelimiter;
    private final String salesmanPrefix;
    private final String customerPrefix;
    private final String salePrefix;
    private final String outputDirectory;
    private final String filesExtension;
    
    private final List<String> customersData = new ArrayList<>();
    private final List<Sale> salesData = new ArrayList<>();
    private final List<Salesman> salesmenData = new ArrayList<>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);
    
    protected void setFile(File file) {
        this.file = file;
    }
    
    @Autowired
    private FileHandler(@Value("${list.delimiter}") String listDelimiter,
                        @Value("${products.info.delimiter}") String productsInfoDelimiter,
                        @Value("${general.delimiter}") String generalDelimiter,
                        @Value("${salesman.prefix}") String salesmanPrefix,
                        @Value("${customer.prefix}") String customerPrefix,
                        @Value("${sale.prefix}") String salePrefix,
                        @Value("${output.directory}") String outputDirectory,
                        @Value("${files.extension}") String filesExtension) {
        this.listDelimiter = listDelimiter;
        this.productsInfoDelimiter = productsInfoDelimiter;
        this.generalDelimiter = generalDelimiter;
        this.salesmanPrefix = salesmanPrefix;
        this.customerPrefix = customerPrefix;
        this.salePrefix = salePrefix;
        this.outputDirectory = outputDirectory;
        this.filesExtension = filesExtension;
    }
    
    @Override
    public void run() {
        LOGGER.info("Reading file :" + file.getName());
        readFile(file);
        LOGGER.info("Writing output from file :" + file.getName());
        writeReport(file);
    }
    
    private void readFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String currentElement = scanner.next();
                if (currentElement.startsWith(salesmanPrefix)) {
                    salesmenData.add(
                            tailorSalesmanData(currentElement)
                                    );
                } else if (currentElement.startsWith(customerPrefix)) {
                    customersData.add(currentElement);
                } else if (currentElement.startsWith(salePrefix)) {
                    salesData.add(
                            tailorSaleData(currentElement)
                                 );
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not read file: " + file.getName());
            LOGGER.trace(e.toString());
        }
    }
    
    private Sale tailorSaleData(String data) {
        BigDecimal salePrice = BigDecimal.ZERO;
        List<String> saleInfo = List.of(data.split(generalDelimiter));
        List<String> products = List.of(saleInfo.get(2)
                                                .replace("[", "")
                                                .replace("]", "")
                                                .split(listDelimiter));
        for (String product : products) {
            List<String> productInfo = List.of(product.split(productsInfoDelimiter));
            BigDecimal quantity = BigDecimal.valueOf(Float.parseFloat(productInfo.get(1)));
            BigDecimal price = BigDecimal.valueOf(Float.parseFloat(productInfo.get(2)));
            salePrice = salePrice.add(price.multiply(quantity));
        }
        return SaleBuilder.builder()
                          .withId(Long.parseLong(saleInfo.get(1)))
                          .withSalesman(saleInfo.get(3))
                          .withPrice(salePrice)
                          .build();
    }
    
    private Salesman tailorSalesmanData(String data) {
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
                              .withSalary(BigDecimal.valueOf(Float.parseFloat(salesmanInfo.get(3))))
                              .withAmountSold(amountSold)
                              .build();
    }
    
    private Long getMostExpensiveSaleId() {
        Sale mostExpansiveSale = SaleBuilder.builder()
                                            .withPrice(BigDecimal.ZERO)
                                            .build();
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
    
    private Salesman getWorstSalesmanEver() {
        List<Salesman> tempData = salesmenData;
        tempData.sort(Comparator.comparing(Salesman::getAmountSold));
        return tempData.get(0);
    }
    
    private int getSalesmenAmount() {
        return salesmenData.size();
    }
    
    private int getCustomersAmount() {
        return customersData.size();
    }
    
    private void writeReport(File file) {
        String fileName = file.getName().replace(filesExtension, ".done" + filesExtension);
        File dir2 = new File(outputDirectory);
        File outputFile = new File(dir2, fileName);
        try (Writer output = new BufferedWriter(new FileWriter(outputFile))) {
            output.write("The total amount of customers is: " + getCustomersAmount() + "\n" +
                         "The total amount of salesmen is: " + getSalesmenAmount() + "\n" +
                         "The most expensive sale has ID:" + getMostExpensiveSaleId() + "\n" +
                         "The worst salesman ever is: " + getWorstSalesmanEver() + "\n");
            LOGGER.info("Output file has been written for input file :" + file.getName());
        } catch (Exception e) {
            LOGGER.error("Could not write output file from :" + file.getName());
            LOGGER.trace(e.toString());
        }
    }
}
