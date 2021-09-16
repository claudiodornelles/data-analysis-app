package com.claudiodornelles.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileInterpreterService {
    
    private final String inputDirectory;
    private final String filesExtension;
    private final ApplicationContext context;
    
    @Autowired
    public FileInterpreterService(@Value("${input.directory}") String inputDirectory,
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
            System.out.println("Waiting for new files...");
        } else {
            for (File file : newFiles) {
                writtenOutputs.add(file.getName());
                FileReader fileReader = context.getBean("fileReader", FileReader.class);
                fileReader.setFile(file);
                Thread thread = new Thread(fileReader);
                thread.start();
            }
        }
    }
}
