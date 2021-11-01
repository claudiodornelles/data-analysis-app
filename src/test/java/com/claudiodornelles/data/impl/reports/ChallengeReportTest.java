package com.claudiodornelles.data.impl.reports;

import com.claudiodornelles.data.impl.builders.SaleBuilder;
import com.claudiodornelles.data.impl.builders.SalesmanBuilder;
import com.claudiodornelles.data.impl.config.AppConfig;
import com.claudiodornelles.data.impl.dao.FileDAO;
import com.claudiodornelles.data.impl.models.Sale;
import com.claudiodornelles.data.impl.models.Salesman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.claudiodornelles.data.impl.reports.config.ChallengeReportTestConfig.fileWithIncompleteInformationResponse;
import static com.claudiodornelles.data.impl.reports.config.ChallengeReportTestConfig.fileWithInvalidPrefixResponse;
import static com.claudiodornelles.data.impl.reports.config.ChallengeReportTestConfig.regularFileResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(AppConfig.class)
class ChallengeReportTest {

    @TempDir
    public static File temporaryDirectory;

    private final File temporaryFile = new File(temporaryDirectory, "tempFile.dat");

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

    @Mock
    private FileDAO fileDAO = mock(FileDAO.class);

    @InjectMocks
    private ChallengeReport challengeReport;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(challengeReport, "customerPrefix", customerPrefix);
        ReflectionTestUtils.setField(challengeReport, "listDelimiter", listDelimiter);
        ReflectionTestUtils.setField(challengeReport, "productsInfoDelimiter", productsInfoDelimiter);
        ReflectionTestUtils.setField(challengeReport, "generalDelimiter", generalDelimiter);
        ReflectionTestUtils.setField(challengeReport, "salesmanPrefix", salesmanPrefix);
        ReflectionTestUtils.setField(challengeReport, "salePrefix", salePrefix);
    }

    @Test
    void shouldBeAbleToReadSource() {
        challengeReport.setSource(temporaryFile);
        when(fileDAO.readFile(temporaryFile)).thenReturn(regularFileResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(regularFileResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(temporaryFile)
                 );
    }

    @Test
    void shouldBeAbleToReadSourceWithInvalidPrefixInformation() {
        challengeReport.setSource(temporaryFile);
        when(fileDAO.readFile(temporaryFile)).thenReturn(fileWithInvalidPrefixResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(fileWithInvalidPrefixResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(temporaryFile)
                 );
    }

    @Test
    void shouldBeAbleToReadSourceWithIncompleteInformation() {
        challengeReport.setSource(temporaryFile);
        when(fileDAO.readFile(temporaryFile)).thenReturn(fileWithIncompleteInformationResponse());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(fileWithIncompleteInformationResponse(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(temporaryFile)
                 );
    }

    @Test
    void shouldBeAbleToGetEmptyListIfAnyErrorOccur() {
        challengeReport.setSource(temporaryFile);
        when(fileDAO.readFile(temporaryFile)).thenReturn(Collections.emptyList());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.readSource()),
                () -> assertEquals(Collections.emptyList(), challengeReport.readSource()),
                () -> verify(fileDAO, times(2)).readFile(temporaryFile)
                 );
    }

    @Test
    void shouldBeAbleToTailorRegularInputs() {
        challengeReport.clearData();
        List<String> customers = new ArrayList<>();
        List<Sale> sales = new ArrayList<>();
        List<Salesman> salesmen = new ArrayList<>();
        customers.add("002ç2345675434544345çJose da SilvaçRural");
        customers.add("002ç2345675433444345çEduardoPereiraçRural");
        sales.add(SaleBuilder.builder()
                             .withId(10L)
                             .withPrice(new BigDecimal("1199.00"))
                             .withSalesman("Diego")
                             .build());
        sales.add(SaleBuilder.builder()
                             .withId(9L)
                             .withPrice(new BigDecimal("2.60"))
                             .withSalesman("João Gonçalves")
                             .build());
        salesmen.add(SalesmanBuilder.builder()
                                    .withName("Diego")
                                    .withCpf("1234567891234")
                                    .withSalary(new BigDecimal("50000"))
                                    .withAmountSold(new BigDecimal("1199.00"))
                                    .build());
        salesmen.add(SalesmanBuilder.builder()
                                    .withName("João Gonçalves")
                                    .withCpf("3245678865434")
                                    .withSalary(new BigDecimal("40000.99"))
                                    .withAmountSold(new BigDecimal("2.60"))
                                    .build());
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.tailorFileData(regularFileResponse())),
                () -> assertEquals(customers, challengeReport.getCustomersData()),
                () -> assertEquals(sales.size(), challengeReport.getSalesData().size()),
                () -> assertEquals(sales.get(0), challengeReport.getSalesData().get(0)),
                () -> assertEquals(sales.get(1), challengeReport.getSalesData().get(1)),
                () -> assertEquals(salesmen.size(), challengeReport.getSalesmenData().size()),
                () -> assertEquals(salesmen.get(0), challengeReport.getSalesmenData().get(0)),
                () -> assertEquals(salesmen.get(1), challengeReport.getSalesmenData().get(1))
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
        challengeReport.setSource(temporaryFile);
        challengeReport.tailorFileData(regularFileResponse());
        String expectedReport = "The total amount of customers is: 2\n" +
                                "The total amount of salesmen is: 2\n" +
                                "The most expensive sale has ID: 10\n" +
                                "The worst salesman ever is: {\"name\":'João Gonçalves', \"cpf\":'3245678865434', \"salary\" :40000.99, \"amountSold\":2.60}\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }

    @Test
    void shouldReturnAReportWithNullElementsIfSourceHasInvalidPrefixInformation() {
        challengeReport.setSource(temporaryFile);
        challengeReport.tailorFileData(fileWithInvalidPrefixResponse());
        String expectedReport = "The total amount of customers is: 0\n" +
                                "The total amount of salesmen is: 0\n" +
                                "The most expensive sale has ID: 0\n" +
                                "The worst salesman ever is: {\"name\":'null', \"cpf\":'null', \"salary\" :null, \"amountSold\":0}\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }

    @Test
    void shouldReturnAReportWithNullElementsIfSourceHasIncompleteInformation() {
        challengeReport.setSource(temporaryFile);
        challengeReport.tailorFileData(fileWithIncompleteInformationResponse());
        String expectedReport = "The total amount of customers is: 0\n" +
                                "The total amount of salesmen is: 0\n" +
                                "The most expensive sale has ID: 0\n" +
                                "The worst salesman ever is: {\"name\":'null', \"cpf\":'null', \"salary\" :null, \"amountSold\":0}\n";
        assertAll(
                () -> assertDoesNotThrow(() -> challengeReport.getReport()),
                () -> assertEquals(expectedReport, challengeReport.getReport())
                 );
    }
}