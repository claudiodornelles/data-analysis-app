package com.claudiodornelles.desafio.dao;

import com.claudiodornelles.desafio.exceptions.FileSizeException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class FileDAO {
    
    private final String salesmanPrefix;
    private final String customerPrefix;
    private final String salePrefix;
    private final String filesExtension;
    private final String outputDirectory;
    private final String generalDelimiter;
    private final long fileByteLimit;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDAO.class);
    
    @Autowired
    public FileDAO(@Value("${salesman.prefix}") String salesmanPrefix,
                   @Value("${customer.prefix}") String customerPrefix,
                   @Value("${sale.prefix}") String salePrefix,
                   @Value("${files.extension}") String filesExtension,
                   @Value("${output.directory}") String outputDirectory,
                   @Value("${general.delimiter}") String generalDelimiter,
                   @Value("${file.byte.limit}") long fileByteLimit) {
        this.salesmanPrefix = salesmanPrefix;
        this.customerPrefix = customerPrefix;
        this.salePrefix = salePrefix;
        this.filesExtension = filesExtension;
        this.outputDirectory = outputDirectory;
        this.generalDelimiter = generalDelimiter;
        this.fileByteLimit = fileByteLimit;
    }
    
    public List<String> readFile(@NotNull File file) {
        String logInfo = "Reading file :" + file.getName();
        LOGGER.info(logInfo);
        try (Scanner scanner = new Scanner(file)) {
            if (file.length() > fileByteLimit) {
                throw new FileSizeException("File size exceeded the limit of " + fileByteLimit + " bytes.");
            }
            List<String> fileData = new ArrayList<>();
            while (scanner.hasNext()) {
                String currentLine = scanner.next();
                List<String> temporaryBlock = List.of(currentLine.split(" "));
                for (String element : temporaryBlock) {
                    int lastElement = fileData.size() - 1;
                    if (element.startsWith(salesmanPrefix) ||
                        element.startsWith(customerPrefix) ||
                        element.startsWith(salePrefix) ||
                        fileData.isEmpty()) {
                        fileData.add(element);
                    } else if (Character.isAlphabetic(element.charAt(0)) &&
                               !element.startsWith(generalDelimiter)) {
                        fileData.set(lastElement, fileData.get(lastElement) + " " + element);
                    } else {
                        fileData.set(lastElement, fileData.get(lastElement) + element);
                    }
                }
            }
            return fileData;
        } catch (Exception e) {
            String logError = "Could not read file: \"" +
                              file.getName() +
                              "\" [ERROR: \"" +
                              e.getMessage() +
                              "\"]";
            LOGGER.error(logError);
            LOGGER.trace(e.toString());
            return Collections.emptyList();
        }
    }
    
    public void writeReport(String report, @NotNull File sourceFile) {
        String logInfo = "Writing report from file :" + sourceFile.getName();
        LOGGER.info(logInfo);
        String fileName = sourceFile.getName().replace(filesExtension, ".done" + filesExtension);
        File dir2 = new File(outputDirectory);
        File outputFile = new File(dir2, fileName);
        try (Writer output = new BufferedWriter(new java.io.FileWriter(outputFile))) {
            output.write(report);
            logInfo = "Report has been written from file:" + sourceFile.getName();
            LOGGER.info(logInfo);
        } catch (Exception e) {
            String logError = "Could not write report from file: \"" +
                              sourceFile.getName() +
                              "\" [ERROR: \"" +
                              e.getMessage() +
                              "\"]";
            LOGGER.error(logError);
            LOGGER.trace(e.toString());
        }
    }
}
