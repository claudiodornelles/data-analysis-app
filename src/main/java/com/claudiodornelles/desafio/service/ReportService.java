package com.claudiodornelles.desafio.service;

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
        List<File> sourceFiles = new ArrayList<>(List.of(Objects.requireNonNull(inputRoot.listFiles((input, name) -> name.endsWith(filesExtension)))));
        sourceFiles.removeIf(file -> writtenOutputs.contains(file.getName()));
        if (sourceFiles.isEmpty()) {
            LOGGER.info("Waiting for new files...");
        } else {
            LOGGER.info("New files available...");
            createReports(sourceFiles);
        }
    }
    
    public void createReports(List<File> sourceFiles) {
        for (File file : sourceFiles) {
            LOGGER.info("Start report from file: " + file.getName());
            writtenOutputs.add(file.getName());
            ReportGenerator reportGenerator = context.getBean("reportGenerator", ReportGenerator.class);
            reportGenerator.setFile(file);
            Thread thread = new Thread(reportGenerator);
            thread.start();
        }
    }
}
