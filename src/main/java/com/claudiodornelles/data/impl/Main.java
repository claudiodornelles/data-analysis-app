package com.claudiodornelles.data.impl;

import com.claudiodornelles.data.impl.config.AppConfig;
import com.claudiodornelles.data.impl.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Creating application context...");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        LOGGER.info("Creating reportService bean...");
        ReportService reportService = applicationContext.getBean("reportService", ReportService.class);
        LOGGER.info("Application started...");
        while (true) {
            try {
                reportService.monitorDirectory();
            } catch (Exception e) {
                LOGGER.trace("Failed to run application", e);
                break;
            }
        }
    }
}
