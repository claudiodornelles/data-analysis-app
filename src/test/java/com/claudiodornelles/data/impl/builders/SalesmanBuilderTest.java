package com.claudiodornelles.data.impl.builders;

import com.claudiodornelles.data.impl.models.Salesman;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesmanBuilderTest {
    
    private Salesman dummySalesman() {
        Salesman dummySalesman = new Salesman();
        dummySalesman.setName("Dummy Salesman");
        dummySalesman.setCpf("123.456.789-00");
        dummySalesman.setSalary(new BigDecimal(1000));
        dummySalesman.setAmountSold(new BigDecimal(5000));
        return dummySalesman;
    }
    
    private Salesman buildDummySalesman() {
        return SalesmanBuilder.builder()
                              .withName("Dummy Salesman")
                              .withCpf("123.456.789-00")
                              .withSalary(new BigDecimal(1000))
                              .withAmountSold(new BigDecimal(5000))
                              .build();
    }
    
    @Test
    void shouldBeAbleToBuildASalesman() {
        assertAll(
                () -> assertDoesNotThrow(this::buildDummySalesman),
                () -> assertEquals(dummySalesman().getName(), buildDummySalesman().getName()),
                () -> assertEquals(dummySalesman().getCpf(), buildDummySalesman().getCpf()),
                () -> assertEquals(dummySalesman().getSalary(), buildDummySalesman().getSalary()),
                () -> assertEquals(dummySalesman().getAmountSold(), buildDummySalesman().getAmountSold()),
                () -> assertEquals(Salesman.class, buildDummySalesman().getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithoutAName() {
        IllegalArgumentException buildSalesmanWithoutNameException = assertThrows(IllegalArgumentException.class,
                                                                                  () -> SalesmanBuilder.builder()
                                                                                                       .withAmountSold(new BigDecimal(1))
                                                                                                       .withSalary(new BigDecimal(1))
                                                                                                       .withCpf("123.456.789-00")
                                                                                                       .build());
        assertAll(
                () -> assertEquals("Cannot create a salesman without a name", buildSalesmanWithoutNameException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSalesmanWithoutNameException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNullName() {
        IllegalArgumentException buildSalesmanWithNullNameException = assertThrows(IllegalArgumentException.class,
                                                                                   () -> SalesmanBuilder.builder()
                                                                                                        .withName(null)
                                                                                                        .withAmountSold(new BigDecimal(1))
                                                                                                        .withSalary(new BigDecimal(1))
                                                                                                        .withCpf("123.456.789-00")
                                                                                                        .build());
        assertAll(
                () -> assertEquals("A name must be passed", buildSalesmanWithNullNameException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSalesmanWithNullNameException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildSalesmanWithBlankName() {
        IllegalArgumentException buildSalesmanWithBlankNameException = assertThrows(IllegalArgumentException.class,
                                                                                    () -> SalesmanBuilder.builder()
                                                                                                         .withName(" ")
                                                                                                         .withAmountSold(new BigDecimal(1))
                                                                                                         .withSalary(new BigDecimal(1))
                                                                                                         .withCpf("123.456.789-00")
                                                                                                         .build());
        assertAll(
                () -> assertEquals("A name must be passed", buildSalesmanWithBlankNameException.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, buildSalesmanWithBlankNameException.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildSalesmanWithoutCpf() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withName("Dummy Salesman")
                                                                               .withSalary(new BigDecimal(1))
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .build());
        assertAll(
                () -> assertEquals("Cannot create a salesman without a CPF", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithBlankCpf() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withSalary(new BigDecimal(1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf(" ")
                                                                               .build());
        assertAll(
                () -> assertEquals("A cpf must be passed", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNullCpf() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withSalary(new BigDecimal(1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf(null)
                                                                               .build());
        assertAll(
                () -> assertEquals("A cpf must be passed", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithoutSalary() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("Cannot create a salesman without a Salary", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNullSalary() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withSalary(null)
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("A salary must be passed", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNegativeSalary() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withSalary(new BigDecimal(-1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("Salary must be greater than zero", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithZeroSalary() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(1))
                                                                               .withSalary(new BigDecimal(0))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("Salary must be greater than zero", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldBuildASalesmanWithAmountSoldZeroIfNoneIsGiven() {
        Salesman salesman = SalesmanBuilder.builder()
                                           .withSalary(new BigDecimal(1))
                                           .withName("Dummy Salesman")
                                           .withCpf("123.456.789-00")
                                           .build();
        assertAll(
                () -> assertEquals(BigDecimal.ZERO, salesman.getAmountSold())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNullAmountSold() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(null)
                                                                               .withSalary(new BigDecimal(1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("An amount sold must be passed", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
    
    @Test
    void shouldNotBeAbleToBuildASalesmanWithNegativeAmountSold() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> SalesmanBuilder.builder()
                                                                               .withAmountSold(new BigDecimal(-1000))
                                                                               .withSalary(new BigDecimal(1))
                                                                               .withName("Dummy Salesman")
                                                                               .withCpf("123.456.789-00")
                                                                               .build());
        assertAll(
                () -> assertEquals("Amount sold cannot be negative", exception.getMessage()),
                () -> assertEquals(IllegalArgumentException.class, exception.getClass())
                 );
    }
}