package com.restdude.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;


public abstract class AbstractPersistenceJPAConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistenceJPAConfig.class);

    public static String getPropertyAsString(Properties prop) {
        StringWriter writer = new StringWriter();
        try {
            prop.store(writer, "");
        } catch (IOException e) {
            LOGGER.warn("Failed listing properties", e);
        }
        return writer.getBuffer().toString();
    }

    @Value("${calipso.ds.driverClass}")
    private String dsDriverClass;

    @Value("${calipso.ds.jdbcUrl}")
    private String dsJdbcUrl;

    @Value("${calipso.ds.username}")
    private String dsUsername;

    @Value("${calipso.ds.password}")
    private String dsPassword;

    @Value("${calipso.persistenceUnit.packagesToScan}")
    private String emPackagesToScan;

    @Value("${calipso.hibernate.dialect}")
    private String emHbmDialect;

    @Value("${calipso.hibernate.show_sql}")
    private String emHbmShowSql;

    @Value("${calipso.hibernate.format_sql}")
    private String emHbmFormatSql;

    @Value("${calipso.hibernate.hbm2ddl.auto}")
    private String emHbm2ddlAuto;

    @Value("${calipso.hibernate.cache.use_second_level_cache}")
    private String emHbmCacheUseSecondLevelCache;

    @Value("${calipso.hibernate.cache.provider_class}")
    private String emHbmCacheProviderClass;

    @Value("${calipso.hibernate.id.new_generator_mappings}")
    private String emHbmIdNewGeneratorMappings;


    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        LOGGER.debug("entityManagerFactory");
        String[] packages = {"com.restdude"};//this.emPackagesToScan.split(",");
        LOGGER.debug("entityManagerFactory setPackagesToScan: {}", Arrays.toString(packages));

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(packages);

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        em.afterPropertiesSet();

        return (EntityManagerFactory) em.getObject();
    }

    @Bean
    public DataSource dataSource() {
        LOGGER.debug("dataSource setDriverClassName: {}", this.dsDriverClass);
        LOGGER.debug("dataSource setUrl: {}", this.dsJdbcUrl);
        LOGGER.debug("dataSource setUsername: {}", this.dsUsername);
        LOGGER.debug("dataSource setPassword: {}", this.dsPassword);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.dsDriverClass);
        dataSource.setUrl(this.dsJdbcUrl);
        dataSource.setUsername(this.dsUsername);
        dataSource.setPassword(this.dsPassword);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        LOGGER.debug("exceptionTranslation");
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        LOGGER.debug("additionalProperties: ");
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", this.emHbmDialect);
        properties.setProperty("hibernate.show_sql", this.emHbmShowSql);
        properties.setProperty("hibernate.format_sql", this.emHbmFormatSql);
        properties.setProperty("hibernate.hbm2ddl.auto", this.emHbm2ddlAuto);
        properties.setProperty("hibernate.cache.use_second_level_cache", this.emHbmCacheUseSecondLevelCache);
        properties.setProperty("hibernate.cache.provider_class", this.emHbmCacheProviderClass);
        properties.setProperty("hibernate.id.new_generator_mappings", this.emHbmIdNewGeneratorMappings);
        properties.setProperty("javax.persistence.validation.mode", "none");

        properties.setProperty("jadira.usertype.autoRegisterUserTypes", Boolean.TRUE.toString());

        if (LOGGER.isDebugEnabled()) {
            for (Object key : properties.keySet()) {
                LOGGER.debug("{}={}", key, properties.getProperty(key.toString()));
            }
        }
        return properties;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}