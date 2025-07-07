package com.piuraexpressa.config.seguridad;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.piuraexpressa.repositorio.seguridad",
    entityManagerFactoryRef = "seguridadEntityManagerFactory",
    transactionManagerRef = "seguridadTransactionManager")
public class SeguridadDataSourceConfig {

    @Primary
    @Bean(name = "seguridadDataSource")
    public DataSource seguridadDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/bd_seguridad");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Leyenda29");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Primary
    @Bean(name = "seguridadEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean seguridadEntityManagerFactory(
            @Qualifier("seguridadDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.piuraexpressa.model.seguridad");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        emf.setJpaPropertyMap(properties);
        return emf;
    }

    @Bean(name = "seguridadTransactionManager")
    public PlatformTransactionManager seguridadTransactionManager(
            @Qualifier("seguridadEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
