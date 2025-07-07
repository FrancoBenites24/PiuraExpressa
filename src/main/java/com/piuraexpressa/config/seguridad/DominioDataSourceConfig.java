package com.piuraexpressa.config.seguridad;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@EntityScan
@Primary
@EnableJpaRepositories(
    basePackages = "com.piuraexpressa.repositorio.dominio",
    entityManagerFactoryRef = "dominioEntityManagerFactory",
    transactionManagerRef = "dominioTransactionManager"
)public class DominioDataSourceConfig {

    @Bean(name = "dominioDataSource")
    public DataSource dominioDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/bd_piuraexpressa");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Leyenda29");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Bean(name = "dominioEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dominioEntityManagerFactory(
            @Qualifier("dominioDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.piuraexpressa.model.dominio");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        emf.setJpaPropertyMap(properties);
        return emf;
    }

    @Bean(name = "dominioTransactionManager")
    public PlatformTransactionManager dominioTransactionManager(
            @Qualifier("dominioEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
