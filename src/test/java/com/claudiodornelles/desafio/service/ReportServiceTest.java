package com.claudiodornelles.desafio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReportServiceTest {
    
    @Autowired
    private ReportService reportService;
    
    @Test
    void shouldNotThrowExceptionCallingMethodMonitorDirectory() {
        assertDoesNotThrow(() -> reportService.monitorDirectory());
    }
    
    @Test
    void shouldNotThrowExceptionCallingMethodCreateReportsForChallengeReport() {
        List<File> sourceList = new ArrayList<>();
        sourceList.add(Paths.get("test").resolve("test.txt").toFile());
        assertDoesNotThrow(() -> reportService.createReports(sourceList, "challengeReport"));
    }
    
    @Test
    void shouldThrowExceptionTryingToCreateReportFromNonExistingTemplate() {
        List<File> sourceList = new ArrayList<>();
        sourceList.add(Paths.get("test").resolve("test.txt").toFile());
        assertThrows(NoSuchBeanDefinitionException.class, () -> reportService.createReports(sourceList, "aeiogjaeigae"));
    }
}