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
public class DataInterpreterService {
    
    private final String inputDirectory;
    private final String filesExtension;
    private final ApplicationContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInterpreterService.class);
    
    @Autowired
    public DataInterpreterService(@Value("${input.directory}") String inputDirectory,
                                  @Value("${files.extension}") String filesExtension,
                                  ApplicationContext context) {
        this.inputDirectory = inputDirectory;
        this.filesExtension = filesExtension;
        this.context = context;
    }
    
    private final List<String> writtenOutputs = new ArrayList<>();
    
    public void readInputData() {
        File inputRoot = new File(inputDirectory);
        List<File> newFiles = new ArrayList<>(List.of(Objects.requireNonNull(inputRoot.listFiles((input, name) -> name.endsWith(filesExtension)))));
        newFiles.removeIf(file -> writtenOutputs.contains(file.getName()));
        if (newFiles.isEmpty()) {
            LOGGER.info("Waiting for new files...");
        } else {
            for (File file : newFiles) {
                LOGGER.info("New file found: " + file.getName());
                writtenOutputs.add(file.getName());
                FileHandler fileHandler = context.getBean("fileHandler", FileHandler.class);
                fileHandler.setFile(file);
                Thread thread = new Thread(fileHandler);
                thread.start();
            }
        }
    }
    
    
}
