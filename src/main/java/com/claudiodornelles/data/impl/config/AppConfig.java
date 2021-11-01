package com.claudiodornelles.data.impl.config;

import com.claudiodornelles.data.impl.dao.FileDAO;
import com.claudiodornelles.data.impl.reports.ChallengeReport;
import com.claudiodornelles.data.impl.service.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("com.claudiodornelles.data.impl")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${list.delimiter}") private String listDelimiter;
    @Value("${products.info.delimiter}") private String productsInfoDelimiter;
    @Value("${general.delimiter}") private String generalDelimiter;
    @Value("${salesman.prefix}") private String salesmanPrefix;
    @Value("${customer.prefix}") private String customerPrefix;
    @Value("${sale.prefix}") private String salePrefix;
    @Value("${files.extension}") private String filesExtension;
    @Value("${input.directory}") private String inputDirectory;
    @Value("${output.directory}") private String outputDirectory;
    @Value("${file.byte.limit}") private String fileByteLimit;

    @Bean("fileDAO")
    @Scope("prototype")
    public FileDAO fileDAO() {
        return new FileDAO(
                salesmanPrefix,
                customerPrefix,
                salePrefix,
                filesExtension,
                outputDirectory,
                generalDelimiter,
                fileByteLimit
        );
    }

    @Bean
    @DependsOn("fileDAO")
    @Scope("prototype")
    public ChallengeReport challengeReport() {
        return new ChallengeReport(
                listDelimiter,
                productsInfoDelimiter,
                generalDelimiter,
                salesmanPrefix,
                customerPrefix,
                salePrefix,
                fileDAO()
        );
    }

    @Bean("reportService")
    public ReportService reportService() {
        return new ReportService(
                inputDirectory,
                filesExtension
        );
    }

}
