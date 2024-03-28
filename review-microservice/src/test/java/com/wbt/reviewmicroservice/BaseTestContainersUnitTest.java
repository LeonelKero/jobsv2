package com.wbt.reviewmicroservice;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
@ActiveProfiles(profiles = {"prod"})
public abstract class BaseTestContainersUnitTest {

    @Container
    protected final static PostgreSQLContainer<?> postgresSqlContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test-database")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    private static void registerDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresSqlContainer::getPassword);
    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgresSqlContainer.getDriverClassName())
                .url(postgresSqlContainer.getJdbcUrl())
                .username(postgresSqlContainer.getUsername())
                .password(postgresSqlContainer.getPassword())
                .build();
    }

    protected static JdbcTemplate restTemplate() {
        return new JdbcTemplate(getDataSource());
    }

}
