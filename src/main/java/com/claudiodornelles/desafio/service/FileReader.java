package com.claudiodornelles.desafio.service;

import com.claudiodornelles.desafio.builders.SaleBuilder;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.repository.DAO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@Scope("prototype")
public class FileReader {
    
    private static final String LIST_DELIMITER = ",";
    private static final String PRODUCTS_INFO_DELIMITER = "-";
    private static final String GENERAL_DELIMITER = "ç";
    private static final String SALESMAN_PREFIX = "001" + GENERAL_DELIMITER;
    private static final String CUSTOMER_PREFIX = "002" + GENERAL_DELIMITER;
    private static final String SALE_PREFIX = "003" + GENERAL_DELIMITER;
    
    private final List<String> customers = new ArrayList<>();
    private final List<String> sales = new ArrayList<>();
    private final List<String> salesmen = new ArrayList<>();
    
    public void read(File file) {
        List<String> tailoredData = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String currentElement = scanner.next();
                if (currentElement.startsWith(SALESMAN_PREFIX) ||
                    currentElement.startsWith(CUSTOMER_PREFIX) ||
                    currentElement.startsWith(SALE_PREFIX)) {
                    tailoredData.add(currentElement);
                } else {
                    int lastElement = tailoredData.size() - 1;
                    tailoredData.set(lastElement, tailoredData.get(lastElement) + " " + currentElement);
                }
                
                for (String block : tailoredData) {
                    if (block.startsWith(SALESMAN_PREFIX)) {
                        salesmen.add(block);
                    } else if (block.startsWith(CUSTOMER_PREFIX)) {
                        customers.add(block);
                    } else if (block.startsWith(SALE_PREFIX)) {
                        sales.add(block);
                    }
                }
                
                tailoredData.clear();
            }
            
            writeOut(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Long getMostExpensiveSaleId() {
        Sale mostExpansiveSale = SaleBuilder.builder()
                                            .withPrice(BigDecimal.ZERO)
                                            .build();
        for (String sale : sales) {
            List<String> saleInfo = List.of(sale.split(GENERAL_DELIMITER));
            BigDecimal thisSalePrice = BigDecimal.ZERO;
            List<String> products = List.of(saleInfo.get(2)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(LIST_DELIMITER));
            for (String product : products) {
                List<String> productInfo = List.of(product.split(PRODUCTS_INFO_DELIMITER));
                BigDecimal quantity = BigDecimal.valueOf(Float.parseFloat(productInfo.get(1)));
                BigDecimal price = BigDecimal.valueOf(Float.parseFloat(productInfo.get(2)));
                thisSalePrice = thisSalePrice.add(price.multiply(quantity));
            }
            
            if (mostExpansiveSale.getPrice().compareTo(thisSalePrice) < 0) {
                mostExpansiveSale = SaleBuilder.builder()
                                               .withId(Long.parseLong(saleInfo.get(1)))
                                               .withPrice(thisSalePrice)
                                               .build();
            }
        }
        
        if (mostExpansiveSale.getId() != null) {
            return mostExpansiveSale.getId();
        } else {
            return null;
        }
    }
    
    public int getCustomersAmount() {
        return customers.size();
    }
    
    public int getSalesmenAmount() {
        return salesmen.size();
    }
    
    
    private void writeOut(File file) {
        int dataAmount = sales.size() + salesmen.size() + customers.size();
        
        String outputDirectory = System.getProperty("user.home") + "/data/out/";
        String fileName = file.getName().replace(".dat", ".done.dat");
        File dir2 = new File(outputDirectory);
        File outputFile = new File(dir2, fileName);
        try (Writer output = new BufferedWriter(new FileWriter(outputFile));) {
            output.write("The total amount of customers is: " + getCustomersAmount() + "\n" +
                         "The total amount of salesmen is: " + getSalesmenAmount() + "\n" +
                         "The most expensive sale ID is:" + getMostExpensiveSaleId() + "\n" +
                         //"The worst salesman ever is: " + dao.getWorstSalesman() + "\n" +
                         "Total data amount: " + dataAmount);
            System.out.println(LocalDateTime.now().toString().replace("T", " ") + ": File \"" + fileName + "\" written.");
        } catch (Exception e) {
            System.out.println("Could not create file");
        }
    }
}
