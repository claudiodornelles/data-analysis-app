package com.claudiodornelles.desafio;

import com.claudiodornelles.desafio.service.DataInterpreterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        DataInterpreterService dataInterpreterService = applicationContext.getBean("dataInterpreterService", DataInterpreterService.class);
        while (true) {
            try {
                dataInterpreterService.readInputData();
                Thread.sleep(5000);
            } catch (Exception e) {
                LOGGER.trace(e.toString());
                break;
            }
        }
    }
}
