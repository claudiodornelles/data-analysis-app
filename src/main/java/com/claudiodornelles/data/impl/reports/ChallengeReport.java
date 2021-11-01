package com.claudiodornelles.data.impl.reports;

import com.claudiodornelles.data.impl.builders.SaleBuilder;
import com.claudiodornelles.data.impl.builders.SalesmanBuilder;
import com.claudiodornelles.data.impl.dao.FileDAO;
import com.claudiodornelles.data.impl.exceptions.EmptyDataException;
import com.claudiodornelles.data.impl.models.Report;
import com.claudiodornelles.data.impl.models.Sale;
import com.claudiodornelles.data.impl.models.Salesman;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ChallengeReport(String listDelimiter,
                           String productsInfoDelimiter,
                           String generalDelimiter,
                           String salesmanPrefix,
                           String customerPrefix,
                           String salePrefix,
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
        try {
            tailorFileData(readSource());
            writeReport();
        } catch (EmptyDataException e) {
            LOGGER.error("File {{}} has no data", file.getName(), e);
        }
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
        try {
            fileDAO.writeReport(getReport(), file);
        } catch (Exception e) {
            LOGGER.error("Error trying to write report", e);
        }
    }

    public void tailorFileData(List<String> fileData) {
        if (fileData.isEmpty()) {
            throw new EmptyDataException("No data has been read from file \"" + file.getName() + "\".");
        } else {
            for (String element : fileData) {
                if (element.startsWith(customerPrefix)) {
                    customersData.add(element);
                } else if (element.startsWith(salePrefix)) {
                    tailorSaleData(element).ifPresent(salesData::add);
                }
            }
            for (String element : fileData) {
                if (element.startsWith(salesmanPrefix)) {
                    tailorSalesmanData(element).ifPresent(salesmenData::add);
                }
            }
        }
    }

    public Optional<Sale> tailorSaleData(String data) {
        try {
            String[] saleInfo = data.split(generalDelimiter);

            BigDecimal salePrice = BigDecimal.ZERO;
            String[] products = saleInfo[2]
                    .replace("[", "")
                    .replace("]", "")
                    .split(listDelimiter);
            for (String product : products) {
                String[] productInfo = product.split(productsInfoDelimiter);
                BigDecimal quantity = new BigDecimal(productInfo[1]);
                BigDecimal price = new BigDecimal(productInfo[2]);
                salePrice = salePrice.add(price.multiply(quantity));
            }


            StringBuilder salesmanName = new StringBuilder(saleInfo[3]);
            if (saleInfo.length > 4) {
                int lastElementIndex = saleInfo.length - 1;
                for (int i = 4; i <= lastElementIndex; i++) {
                    salesmanName.append(generalDelimiter);
                    salesmanName.append(saleInfo[i]);
                }
            }

            return Optional.of(SaleBuilder.builder()
                    .withId(Long.parseLong(saleInfo[1]))
                    .withSalesman(salesmanName.toString())
                    .withPrice(salePrice)
                    .build());
        } catch (Exception e) {
            LOGGER.error("Inconsistent information found: {{}}", data, e);
            return Optional.empty();
        }
    }

    public Optional<Salesman> tailorSalesmanData(String data) {
        try {
            String[] salesmanInfo = data.split(generalDelimiter);
            int lastElementIndex = salesmanInfo.length - 1;
            StringBuilder salesmanName = new StringBuilder(salesmanInfo[2]);
            if (salesmanInfo.length > 4) {
                for (int i = 3; i < lastElementIndex; i++) {
                    salesmanName.append(generalDelimiter);
                    salesmanName.append(salesmanInfo[i]);
                }
            }
            List<Sale> sales = salesData.stream().filter(sale -> sale.getSalesman().equals(salesmanName.toString())).collect(Collectors.toList());
            BigDecimal amountSold = BigDecimal.ZERO;
            for (Sale sale : sales) {
                amountSold = amountSold.add(sale.getPrice());
            }
            return Optional.of(SalesmanBuilder.builder()
                    .withCpf(salesmanInfo[1])
                    .withName(salesmanName.toString())
                    .withSalary(new BigDecimal(salesmanInfo[lastElementIndex]))
                    .withAmountSold(amountSold)
                    .build());
        } catch (Exception e) {
            LOGGER.error("Inconsistent information found: {{}}", data, e);
            return Optional.empty();
        }
    }

    public Long getMostExpensiveSaleId() {
        Sale mostExpansiveSale = new Sale();
        mostExpansiveSale.setPrice(BigDecimal.ZERO);
        for (Sale sale : salesData) {
            if (sale.getPrice().compareTo(mostExpansiveSale.getPrice()) > 0) {
                mostExpansiveSale = sale;
            }
        }
        if (mostExpansiveSale.getId() == 0) {
            LOGGER.error("Method getMostExpensiveSaleId failed: No sale found.");
        }
        return mostExpansiveSale.getId();
    }

    public Optional<Salesman> getWorstSalesmanEver() {
        if (!salesmenData.isEmpty()) {
            List<Salesman> tempData = salesmenData;
            tempData.sort(Comparator.comparing(Salesman::getAmountSold));
            return Optional.of(tempData.get(0));
        } else {
            return Optional.empty();
        }
    }

    public String getReport() {
        return "The total amount of customers is: " + customersData.size() + "\n" +
                "The total amount of salesmen is: " + salesmenData.size() + "\n" +
                "The most expensive sale has ID: " + getMostExpensiveSaleId() + "\n" +
                "The worst salesman ever is: " + getWorstSalesmanEver().orElse(new Salesman()) + "\n";
    }

    public List<String> getCustomersData() {
        return customersData;
    }

    public List<Sale> getSalesData() {
        return salesData;
    }

    public List<Salesman> getSalesmenData() {
        return salesmenData;
    }

    public void clearData() {
        this.customersData.clear();
        this.salesData.clear();
        this.salesmenData.clear();
    }
}
