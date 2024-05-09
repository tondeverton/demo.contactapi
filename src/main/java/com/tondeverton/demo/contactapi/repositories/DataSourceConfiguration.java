package com.tondeverton.demo.contactapi.repositories;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class DataSourceConfiguration {

    @ConditionalOnProperty(name = "spring.datasource.target", havingValue = "postgres")
    @Bean
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @ConditionalOnProperty(name = "spring.datasource.target", havingValue = "postgres")
    @Bean
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSource postgresDataSource() {
        return postgresDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @ConditionalOnProperty(name = "spring.datasource.target", havingValue = "h2")
    @Bean
    @ConfigurationProperties("spring.datasource.h2")
    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @ConditionalOnProperty(name = "spring.datasource.target", havingValue = "h2")
    @Bean
    @ConfigurationProperties("spring.datasource.h2")
    public DataSource h2DataSource() {
        return postgresDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
