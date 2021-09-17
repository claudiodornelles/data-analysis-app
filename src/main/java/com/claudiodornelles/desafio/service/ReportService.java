package com.claudiodornelles.desafio.service;

import com.claudiodornelles.desafio.models.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService {
    
    private final String inputDirectory;
    private final String filesExtension;
    private final ApplicationContext context;
    private final List<String> writtenOutputs = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    
    
    @Autowired
    public ReportService(@Value("${input.directory}") String inputDirectory,
                         @Value("${files.extension}") String filesExtension,
                         ApplicationContext context) {
        this.inputDirectory = inputDirectory;
        this.filesExtension = filesExtension;
        this.context = context;
    }
    
    public void monitorDirectory() {
        File inputRoot = new File(inputDirectory);
        List<File> sourceFiles = new ArrayList<>(List.of(Objects.requireNonNull(inputRoot.listFiles((file, name) -> name.endsWith(filesExtension)))));
        sourceFiles.removeIf(file -> writtenOutputs.contains(file.getName()));
        if (!sourceFiles.isEmpty()) {
            LOGGER.info("New files available...");
            createReports(sourceFiles, "challengeReport");
        }
    }
    
    public void createReports(List<File> sourceFiles, String reportType) {
        try {
            for (File file : sourceFiles) {
                LOGGER.info("Creating " + reportType + " from file: " + file.getName());
                writtenOutputs.add(file.getName());
                Report report = context.getBean(reportType, Report.class);
                report.setSource(file);
                Thread thread = new Thread((Runnable) report);
                thread.start();
            }
        } catch (Exception e) {
            LOGGER.error("Cloud not create the report");
            LOGGER.trace(e.toString());
        }
    }
}
