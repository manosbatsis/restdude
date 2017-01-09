/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-war-overlay, https://manosbatsis.github.io/restdude/restdude-war-overlay
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


import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.util.SecurityUtil;
import org.javers.core.Javers;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.SpringSecurityAuthorProvider;
import org.javers.spring.auditable.aspect.JaversAuditableAspect;
import org.javers.spring.auditable.aspect.springdata.JaversSpringDataAuditableRepositoryAspect;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
//@ComponentScan(basePackages = "**.calipso")
//@EnableTransactionManagement
//@EnableAspectJAutoProxy
//@EnableJpaRepositories(basePackages = "org.javers.spring.repository.jpa")
public class AuditJaversConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditJaversConfig.class);

    private static Map<String, DialectName> dialectsMap = new HashMap<String, DialectName>();

    static {
        // TODO complete mappings
        dialectsMap.put("org.hibernate.dialect.SQLServerDialect", DialectName.MSSQL);
        dialectsMap.put("org.hibernate.dialect.H2Dialect", DialectName.H2);
        dialectsMap.put("org.hibernate.dialect.PostgreSQLDialect", DialectName.POSTGRES);
        dialectsMap.put("org.hibernate.dialect.PostgreSQL9Dialect", DialectName.POSTGRES);

        dialectsMap.put("ORACLE", DialectName.ORACLE);
        dialectsMap.put("org.hibernate.dialect.MySQL5InnoDBDialect", DialectName.MYSQL);
    }


    @Autowired
    @Named("dataSource")
    DataSource dataSource;

    @Value("${restdude.hibernate.dialect}")
    private String dialect;

    /**
     * Creates JaVers instance with {@link JaversSqlRepository}
     */
    @Bean
    public Javers javers() {
        DialectName dialect = dialectsMap.get(this.dialect);
        LOGGER.info("Hbm dialect: {}, name: {}", this.dialect, dialect);
        JaversSqlRepository sqlRepository = SqlRepositoryBuilder
                .sqlRepository()
                .withConnectionProvider(jpaConnectionProvider())
                .withDialect(dialect)
                .build();

        return TransactionalJaversBuilder
                .javers()
                .withObjectAccessHook(new HibernateUnproxyObjectAccessHook())
                .registerJaversRepository(sqlRepository)
                .build();
    }

    /**
     * Enables auto-audit aspect for ordinary repositories.<br/>
     * <p>
     * Use {@link org.javers.spring.annotation.JaversAuditable}
     * to mark data writing methods that you want to audit.
     */
    @Bean
    public JaversAuditableAspect javersAuditableAspect() {
        return new JaversAuditableAspect(javers(), authorProvider()/*, commitPropertiesProvider()*/);
    }

    /**
     * Enables auto-audit aspect for Spring Data repositories. <br/>
     * <p>
     * Use {@link org.javers.spring.annotation.JaversSpringDataAuditable}
     * to annotate CrudRepositories you want to audit.
     */
    @Bean
    public JaversSpringDataAuditableRepositoryAspect javersSpringDataAuditableAspect() {
        return new JaversSpringDataAuditableRepositoryAspect(javers(), authorProvider()/*, commitPropertiesProvider()*/);
    }

    /**
     * Required by auto-audit aspect. <br/><br/>
     * <p>
     * Creates {@link SpringSecurityAuthorProvider} instance,
     * suitable when using Spring Security
     */
    @Bean
    public AuthorProvider authorProvider() {
        return new UserDetailsIdgSecurityAuthorProvider();
    }

//    /**
//     * Optional for auto-audit aspect. <br/>
//     * @see CommitPropertiesProvider
//     */
//    @Bean
//    public CommitPropertiesProvider commitPropertiesProvider() {
//        return new CommitPropertiesProvider() {
//            @Override
//            public Map<String, String> provide() {
//                return ImmutableMap.of("key", "ok");
//            }
//        };
//    }

    /**
     * Integrates {@link JaversSqlRepository} with Spring {@link JpaTransactionManager}
     */
    @Bean
    public ConnectionProvider jpaConnectionProvider() {
        return new JpaHibernateConnectionProvider();
    }
    //.. EOF JaVers setup ..

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Provider the current principal UUID as the author
     */
    public static class UserDetailsIdgSecurityAuthorProvider implements AuthorProvider {
        @Override
        public String provide() {
            Optional<ICalipsoUserDetails> ud = SecurityUtil.getPrincipalOptional();
            return ud.isPresent() ? ud.get().getId() : "unauthenticated";
        }
    }

}