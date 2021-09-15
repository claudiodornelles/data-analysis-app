package com.claudiodornelles.desafio.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class FileInterpreterService {
    
    public List<String> readInputData() {
        String home = System.getProperty("user.home");
        File file = new File(home + "/data/in/flatfile.dat");
        List<String> fileData = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String currentElement = scanner.next();
                if (currentElement.startsWith("001ç") ||
                    currentElement.startsWith("002ç") ||
                    currentElement.startsWith("003ç")) {
                    fileData.add(currentElement);
                } else {
                    int lastElement = fileData.size() - 1;
                    fileData.set(lastElement, fileData.get(lastElement) + " " + currentElement);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }
}
