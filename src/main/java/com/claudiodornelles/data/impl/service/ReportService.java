package com.claudiodornelles.data.impl.service;

import com.claudiodornelles.data.impl.models.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReportService {

    private final List<File> writtenFiles = new ArrayList<>();
    private final List<File> sourceFiles = new ArrayList<>();
    private final String inputDirectory;
    private final String filesExtension;

    @Autowired
    private ApplicationContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    public ReportService(String inputDirectory,
                         String filesExtension) {
        this.inputDirectory = inputDirectory;
        this.filesExtension = filesExtension;
    }

    public void monitorDirectory() throws InterruptedException {
        try {
            File inputRoot = new File(inputDirectory);
            sourceFiles.addAll(Arrays.asList(Objects.requireNonNull(inputRoot.listFiles((file, name) -> name.endsWith(filesExtension)))));
            sourceFiles.removeIf(writtenFiles::contains);
            if (!sourceFiles.isEmpty()) {
                LOGGER.info("New files available: {}", sourceFiles);
                createReports("challengeReport");
            }
        } catch (NullPointerException nullPointerException) {
            LOGGER.info("No files found at {}, application will scan directory again in 5 seconds", inputDirectory);
            Thread.sleep(5000);
        } catch (UnsupportedOperationException unsupportedOperationException) {
            LOGGER.error("Could not remove file from source list", unsupportedOperationException);
        }
    }

    public void createReports(String reportType) {
        for (File file : sourceFiles) {
            LOGGER.info("Creating report from file: {}", file.getName());
            writtenFiles.add(file);
            Report report = context.getBean(reportType, Report.class);
            report.setSource(file);
            Thread thread = new Thread((Runnable) report);
            thread.start();
        }
    }
}
