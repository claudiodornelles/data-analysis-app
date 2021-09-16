package com.claudiodornelles.desafio;

import com.claudiodornelles.desafio.service.FileInterpreterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        FileInterpreterService fileInterpreterService = applicationContext.getBean("fileInterpreterService", FileInterpreterService.class);
        while (true) {
            try {
                fileInterpreterService.readInputData();
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
