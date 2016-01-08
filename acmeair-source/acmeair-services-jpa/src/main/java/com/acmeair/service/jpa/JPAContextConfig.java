package com.acmeair.service.jpa;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = { "com.acmeair.service.jpa" })
@EnableJpaRepositories
@EnableTransactionManagement
@PropertySource("classpath:jpa-config.properties")
public class JPAContextConfig {

	@Autowired
	private Environment environment;

	private static final String PROPERTY_NAME_DB_DRIVER = "database.driver";
	private static final String PROPERTY_NAME_DB_URL = "database.url";
	private static final String PROPERTY_NAME_DB_USER = "database.user";
	private static final String PROPERTY_NAME_DB_PASSWORD = "database.password";
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

	@Bean
	public DataSource getDataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(environment
				.getRequiredProperty(PROPERTY_NAME_DB_DRIVER));
		source.setUrl(environment.getRequiredProperty(PROPERTY_NAME_DB_URL));
		source.setUsername(environment.getRequiredProperty(PROPERTY_NAME_DB_USER));
		source.setPassword(environment.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));
		return source;
	}

	/*
	 * @Bean public PersistenceExceptionTranslationPostProcessor
	 * createExcpetionTpp() { return new
	 * PersistenceExceptionTranslationPostProcessor(); }
	 */

	@Bean
	public JpaVendorAdapter getVendorAdapter() {
		JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		/*
		 * Map<String, Object> map = (Map<String, Object>) adapter
		 * .getJpaPropertyMap();
		 */
		// map.put("database", "MYSQL");
		return adapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(getDataSource());

		entityManagerFactoryBean.setJpaVendorAdapter(getVendorAdapter());

		/*
		 * entityManagerFactoryBean
		 * .setPersistenceProviderClass(HibernatePersistence.class);
		 */

		entityManagerFactoryBean
				.setPackagesToScan(environment
						.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));

		entityManagerFactoryBean.setJpaProperties(hibProperties());

		return entityManagerFactoryBean;

	}

	private Properties hibProperties() {

		Properties properties = new Properties();

		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, environment
				.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));

		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, environment
				.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));

		return properties;

	}

	@Bean
	public JpaTransactionManager transactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(entityManagerFactory()
				.getObject());

		return transactionManager;

	}

}
