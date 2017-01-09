/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-framework, https://manosbatsis.github.io/restdude/restdude-framework
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.config;

import com.restdude.domain.users.model.User;
import com.restdude.mdd.util.EntityUtil;
import com.restdude.util.audit.AuditorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
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
import java.util.Set;

/**
 * Extend t configure JPA. Example:
 *
 * <pre>
 * {@code
 * @Ex@Configuration
 * @EnableTransactionManagement
 * @EnableJpaRepositories(basePackages = {"**.restdude"},
 *     repositoryFactoryBeanClass = com.restdude.domain.base.repository.ModelRepositoryFactoryBean.class,
 *     repositoryBaseClass = com.restdude.domain.base.repository.BaseRepositoryImpl.class
 * )
 * @EnableJpaAuditing
 * public class PersistenceJPAConfig extends AbstractPersistenceJPAConfig{
 *
 * }
 * }
 * </pre>
 *
 *
 */
public class AbstractPersistenceJPAConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistenceJPAConfig.class);
    public static final String[] BASE_PACKAGES = {"**.restdude", "**.calipso"};

    public static String getPropertyAsString(Properties prop) {
        StringWriter writer = new StringWriter();
        try {
            prop.store(writer, "");
        } catch (IOException e) {
            LOGGER.warn("Failed listing properties", e);
        }
        return writer.getBuffer().toString();
    }

    @Value("${restdude.ds.driverClass}")
    private String dsDriverClass;

    @Value("${restdude.ds.jdbcUrl}")
    private String dsJdbcUrl;

    @Value("${restdude.ds.username}")
    private String dsUsername;

    @Value("${restdude.ds.password}")
    private String dsPassword;

    @Value("${restdude.persistenceUnit.packagesToScan}")
    private String emPackagesToScan;

    @Value("${restdude.hibernate.dialect}")
    private String emHbmDialect;

    @Value("${restdude.hibernate.show_sql}")
    private String emHbmShowSql;

    @Value("${restdude.hibernate.format_sql}")
    private String emHbmFormatSql;

    @Value("${restdude.hibernate.hbm2ddl.auto}")
    private String emHbm2ddlAuto;

    @Value("${restdude.hibernate.cache.use_second_level_cache}")
    private String emHbmCacheUseSecondLevelCache;

    @Value("${restdude.hibernate.cache.provider_class}")
    private String emHbmCacheProviderClass;

    @Value("${restdude.hibernate.id.new_generator_mappings}")
    private String emHbmIdNewGeneratorMappings;

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        // expand package patterns because
        // org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.setPackagesToScan()
        // does not handle wildcards
        Set<String> entityPackageNames = EntityUtil.findEntityPackageNames(BASE_PACKAGES);
        String[] entityPackages = entityPackageNames.toArray(new String[entityPackageNames.size()]);
        LOGGER.debug("entityManagerFactory setPackagesToScan: {}", Arrays.toString(entityPackages));
        em.setPackagesToScan(entityPackages);

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        em.afterPropertiesSet();

        return (EntityManagerFactory) em.getObject();
    }

    @Bean("dataSource")
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

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorBean();
    }
}
