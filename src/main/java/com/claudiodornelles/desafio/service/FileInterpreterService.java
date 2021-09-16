package com.claudiodornelles.desafio.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class FileInterpreterService {
    
    public void readInputData() {
        String directory = System.getProperty("user.home") + "/data/in/";
        File dir = new File(directory);
        List<File> files = List.of(Objects.requireNonNull(dir.listFiles((dir1, name) -> name.endsWith(".dat"))));
        for (File file : files) {
            System.out.println(LocalDateTime.now().toString().replace("T", " ") + ": Start reading file \"" + file.getName() + "\".");
            FileReader fileReader = new FileReader();
            fileReader.read(file);
            System.out.println(LocalDateTime.now().toString().replace("T", " ") + ": File \"" + file.getName() + "\" read.");
        }
    }
}
