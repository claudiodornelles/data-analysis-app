package com.claudiodornelles.data.impl.dao;

import com.claudiodornelles.data.impl.exceptions.FileSizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileDAO {
    
    private final String salesmanPrefix;
    private final String customerPrefix;
    private final String salePrefix;
    private final String filesExtension;
    private final String outputDirectory;
    private final String generalDelimiter;
    private final String fileByteLimit;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDAO.class);

    public FileDAO(String salesmanPrefix,
                   String customerPrefix,
                   String salePrefix,
                   String filesExtension,
                   String outputDirectory,
                   String generalDelimiter,
                   String fileByteLimit) {
        this.salesmanPrefix = salesmanPrefix;
        this.customerPrefix = customerPrefix;
        this.salePrefix = salePrefix;
        this.filesExtension = filesExtension;
        this.outputDirectory = outputDirectory;
        this.generalDelimiter = generalDelimiter;
        this.fileByteLimit = fileByteLimit;
    }
    
    public List<String> readFile(File file) {
        LOGGER.info("Reading file: {}", file.getName());
        try (Scanner scanner = new Scanner(file)) {
            if (file.length() > Long.parseLong(fileByteLimit)) {
                throw new FileSizeException("File size exceeded the limit of " + fileByteLimit + " bytes.");
            }
            List<String> fileData = new ArrayList<>();
            while (scanner.hasNext()) {
                String currentLine = scanner.next();
                String[] temporaryBlock = currentLine.split(" ");
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
            LOGGER.error("Could not read file: {}", file.getName(), e);
            return Collections.emptyList();
        }
    }
    
    public void writeReport(String report, File sourceFile) {
        LOGGER.info("Writing report from file : {}", sourceFile.getName());
        String fileName = sourceFile.getName().replace(filesExtension, ".done" + filesExtension);
        File dir2 = new File(outputDirectory);
        File outputFile = new File(dir2, fileName);
        try (Writer output = new BufferedWriter(new java.io.FileWriter(outputFile))) {
            output.write(report);
            LOGGER.info("Report has been written from file: {}", sourceFile.getName());
        } catch (Exception e) {
            LOGGER.error("Could not write report from file: {}", sourceFile.getName(), e);
            LOGGER.trace(e.toString());
        }
    }
}
