package com.claudiodornelles.desafio;

import com.claudiodornelles.desafio.service.FileInterpreterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Main {
    
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
    
        FileInterpreterService fileInterpreterService = applicationContext.getBean("fileInterpreterService", FileInterpreterService.class);
        
        fileInterpreterService.readInputData();
    }
}
