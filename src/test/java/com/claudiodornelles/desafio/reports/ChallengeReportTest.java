package com.claudiodornelles.desafio.reports;

import com.claudiodornelles.desafio.builders.SaleBuilder;
import com.claudiodornelles.desafio.builders.SalesmanBuilder;
import com.claudiodornelles.desafio.dao.FileDAO;
import com.claudiodornelles.desafio.models.Sale;
import com.claudiodornelles.desafio.models.Salesman;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChallengeReportTest {
    
    @Mock
    private FileDAO fileDAO = mock(FileDAO.class);
    
    @InjectMocks
    private ChallengeReport challengeReport;
    
    private List<String> regularFileResponse() {
        List<String> response = new ArrayList<>();
        response.add("001ç1234567891234çDiegoç50000");
        response.add("001ç3245678865434çRenatoç40000.99");
        response.add("002ç2345675434544345çJose da SilvaçRural");
        response.add("002ç2345675433444345çEduardoPereiraçRural");
        response.add("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        response.add("003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        return response;
    }
    
    private List<String> fileWithInvalidPrefixResponse() {
        List<String> response = new ArrayList<>();
        response.add("018ç1234567891234çDiegoç50000");
        response.add("015ç3245678865434çRenatoç40000.99");
        response.add("012ç2345675434544345çJose da SilvaçRural");
        response.add("032ç2345675433444345çEduardoPereiraçRural");
        response.add("063ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        response.add("803ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        return response;
    }
    
    private List<String> fileWithIncompleteInformationResponse() {
        List<String> response = new ArrayList<>();
        response.add("018çDiegoç50000");
        response.add("015ç3245678865434ç40000.99");
        response.add("012ç2345675434544345çRural");
        response.add("032ç2345675433444345çEduardoPereira");
        response.add("063ç10ç[1-10-100,2-30-2.50,3-40-3.10]");
        response.add("08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        return response;
    }
    
    @Value("${list.delimiter}")
    private String listDelimiter;
    
    @Value("${products.info.delimiter}")
    private String productsInfoDelimiter;
    
    @Value("${general.delimiter}")
    private String generalDelimiter;
    
    @Value("${salesman.prefix}")
    private String salesmanPrefix;
    
    @Value("${customer.prefix}")
    private String customerPrefix;
    
    @Value("${sale.prefix}")
    private String salePrefix;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(challengeReport, "listDelimiter", listDelimiter);
        ReflectionTestUtils.setField(challengeReport, "productsInfoDelimiter", productsInfoDelimiter);
        ReflectionTestUtils.setField(challengeReport, "generalDelimiter", generalDelimiter);
        ReflectionTestUtils.setField(challengeReport, "salesmanPrefix", salesmanPrefix);
        ReflectionTestUtils.setField(challengeReport, "customerPrefix", customerPrefix);
        ReflectionTestUtils.setField(challengeReport, "salePrefix", salePrefix);
    }
    
    @Test
    void shouldBeAbleToReadSource() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        when(fileDAO.readFile(file)).thenReturn(regularFileResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(regularFileResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(file)
                 );
    }
    
    @Test
    void shouldBeAbleToReadSourceWithInvalidPrefixInformation() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        when(fileDAO.readFile(file)).thenReturn(fileWithInvalidPrefixResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(fileWithInvalidPrefixResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(file)
                 );
    }
    
    @Test
    void shouldBeAbleToReadSourceWithIncompleteInformation() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        when(fileDAO.readFile(file)).thenReturn(fileWithIncompleteInformationResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(fileWithIncompleteInformationResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(file)
                 );
    }
    
    @Test
    void shouldBeAbleToGetEmptyListIfAnyErrorOccur() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        when(fileDAO.readFile(file)).thenReturn(Collections.emptyList());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(Collections.emptyList(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(file)
                 );
    }
    
    @Test
    void shouldBeAbleToTailorRegularInputs() {
        List<String> customers = new ArrayList<>();
        List<Sale> sales = new ArrayList<>();
        List<Salesman> salesmen = new ArrayList<>();
        customers.add("002ç2345675434544345çJose da SilvaçRural");
        customers.add("002ç2345675433444345çEduardoPereiraçRural");
        sales.add(SaleBuilder.builder()
                             .withId(10L)
                             .withPrice(new BigDecimal("1199"))
                             .withSalesman("Diego")
                             .build());
        sales.add(SaleBuilder.builder()
                             .withId(8L)
                             .withPrice(new BigDecimal("393.50"))
                             .withSalesman("Renato")
                             .build());
        salesmen.add(SalesmanBuilder.builder()
                                    .withName("Diego")
                                    .withCpf("1234567891234")
                                    .withSalary(new BigDecimal("50000"))
                                    .withAmountSold(new BigDecimal("1199.00"))
                                    .build());
        salesmen.add(SalesmanBuilder.builder()
                                    .withName("Renato")
                                    .withCpf("3245678865434")
                                    .withSalary(new BigDecimal("40000.99"))
                                    .withAmountSold(new BigDecimal("393.50"))
                                    .build());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.tailorFileData(regularFileResponse())),
                () -> assertEquals(customers.toString(), challengeReport.getCustomersData().toString()),
                () -> assertEquals(sales.size(), challengeReport.getSalesData().size()),
                () -> assertEquals(sales.get(0).getId(), challengeReport.getSalesData().get(0).getId()),
                () -> assertEquals(sales.get(0).getSalesman(), challengeReport.getSalesData().get(0).getSalesman()),
                () -> assertEquals(0, sales.get(0).getPrice().compareTo(challengeReport.getSalesData().get(0).getPrice())),
                () -> assertEquals(sales.get(1).getId(), challengeReport.getSalesData().get(1).getId()),
                () -> assertEquals(sales.get(1).getSalesman(), challengeReport.getSalesData().get(1).getSalesman()),
                () -> assertEquals(0, sales.get(1).getPrice().compareTo(challengeReport.getSalesData().get(1).getPrice())),
                () -> assertEquals(salesmen.size(), challengeReport.getSalesmenData().size()),
                () -> assertEquals(salesmen.get(0).getName(), challengeReport.getSalesmenData().get(0).getName()),
                () -> assertEquals(salesmen.get(0).getCpf(), challengeReport.getSalesmenData().get(0).getCpf()),
                () -> assertEquals(0, salesmen.get(0).getAmountSold().compareTo(challengeReport.getSalesmenData().get(0).getAmountSold())),
                () -> assertEquals(0, salesmen.get(0).getSalary().compareTo(challengeReport.getSalesmenData().get(0).getSalary())),
                () -> assertEquals(salesmen.get(1).getName(), challengeReport.getSalesmenData().get(1).getName()),
                () -> assertEquals(salesmen.get(1).getCpf(), challengeReport.getSalesmenData().get(1).getCpf()),
                () -> assertEquals(0, salesmen.get(1).getAmountSold().compareTo(challengeReport.getSalesmenData().get(1).getAmountSold())),
                () -> assertEquals(0, salesmen.get(1).getSalary().compareTo(challengeReport.getSalesmenData().get(1).getSalary()))
                 );
    }
    
    @Test
    void shouldReturnEmptyListsOfDataIfSourceContainsInvalidPrefix() {
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.tailorFileData(fileWithInvalidPrefixResponse())),
                () -> assertEquals(Collections.emptyList(), challengeReport.getCustomersData()),
                () -> assertEquals(Collections.emptyList(), challengeReport.getSalesmenData()),
                () -> assertEquals(Collections.emptyList(), challengeReport.getSalesData())
                 );
    }
    
    @Test
    void shouldReturnEmptyListOfDataIfSourceHasIncompleteInformation() {
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.tailorFileData(fileWithIncompleteInformationResponse())),
                () -> assertEquals(Collections.emptyList(), challengeReport.getCustomersData()),
                () -> assertEquals(Collections.emptyList(), challengeReport.getSalesmenData()),
                () -> assertEquals(Collections.emptyList(), challengeReport.getSalesData())
                 );
    }
    
    @Test
    void shouldBeAbleToGetReportInformation() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        challengeReport.tailorFileData(regularFileResponse());
        when(fileDAO.readFile(file)).thenReturn(regularFileResponse());
        String expectedReport = "The total amount of customers is: 2\n" +
                                "The total amount of salesmen is: 2\n" +
                                "The most expensive sale has ID: 10\n" +
                                "The worst salesman ever is: {\"name\":'Renato', \"cpf\":'3245678865434', \"salary\" :40000.99, \"amountSold\":393.50}\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }
    
    @Test
    void shouldReturnAReportWithNullElementsIfSourceHasInvalidPrefixInformation() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        challengeReport.tailorFileData(fileWithInvalidPrefixResponse());
        when(fileDAO.readFile(file)).thenReturn(fileWithInvalidPrefixResponse());
        String expectedReport = "The total amount of customers is: 0\n" +
                                "The total amount of salesmen is: 0\n" +
                                "The most expensive sale has ID: null\n" +
                                "The worst salesman ever is: null\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }
    
    @Test
    void shouldReturnAReportWithNullElementsIfSourceHasIncompleteInformation() {
        File file = Paths.get("test").resolve("test.txt").toFile();
        challengeReport.setSource(file);
        challengeReport.tailorFileData(fileWithIncompleteInformationResponse());
        when(fileDAO.readFile(file)).thenReturn(fileWithIncompleteInformationResponse());
        String expectedReport = "The total amount of customers is: 0\n" +
                                "The total amount of salesmen is: 0\n" +
                                "The most expensive sale has ID: null\n" +
                                "The worst salesman ever is: null\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }
}