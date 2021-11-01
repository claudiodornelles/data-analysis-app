package com.claudiodornelles.data.impl.service;

import com.claudiodornelles.data.impl.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(AppConfig.class)
class ReportServiceTest {

    @Autowired
    private ReportService reportService;
    
    @Test
    void shouldNotThrowExceptionCallingMethodMonitorDirectory() {
        assertDoesNotThrow(() -> reportService.monitorDirectory());
    }
    
    @Test
    void shouldNotThrowExceptionCallingMethodCreateReportsForChallengeReport() {
        assertDoesNotThrow(() -> reportService.createReports("challengeReport"));
    }
    
    @Test
    void shouldThrowExceptionTryingToCreateReportFromNonExistingTemplate() {
        NoSuchBeanDefinitionException exception = assertThrows(NoSuchBeanDefinitionException.class, () -> reportService.createReports("aeiogjaeigae"));
        assertEquals("No bean named 'aeiogjaeigae' available", exception.getMessage());
    }
}