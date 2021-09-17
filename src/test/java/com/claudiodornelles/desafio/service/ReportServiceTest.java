package com.claudiodornelles.desafio.service;

import com.claudiodornelles.desafio.models.Report;
import com.claudiodornelles.desafio.reports.ChallengeReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReportServiceTest {
    
    @InjectMocks
    private ReportService reportService;
    
    @Mock
    private ApplicationContext context = mock(ApplicationContext.class);
    
    @Mock
    private ChallengeReport challengeReport = mock(ChallengeReport.class);
    
    @Value("${input.directory}")
    private String inputDirectory;
    
    @Value("${files.extension}")
    private String filesExtension;
    
    @BeforeEach
    void setUp() {
        when(context.getBean("challengeReport", Report.class)).thenReturn(challengeReport);
        when(context.getBean("aeiogjaeigae", Report.class)).thenThrow(NoSuchBeanDefinitionException.class);
        ReflectionTestUtils.setField(reportService, "inputDirectory", inputDirectory);
        ReflectionTestUtils.setField(reportService, "filesExtension", filesExtension);
    }
    
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