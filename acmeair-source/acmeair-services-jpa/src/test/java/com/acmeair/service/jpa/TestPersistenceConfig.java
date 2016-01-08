package com.acmeair.service.jpa;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@ComponentScan(basePackages = { "com.acmeair" })
@EnableJpaRepositories
@PropertySource("classpath:test-config.properties")
public class TestPersistenceConfig {

	@Autowired
	private Environment env;

	private static final String PROPERTY_NAME_DB_DRIVER = "test.database.driver";
	private static final String PROPERTY_NAME_DB_URL = "test.database.url";
	private static final String PROPERTY_NAME_DB_USER = "test.database.user";
	private static final String PROPERTY_NAME_DB_PASSWORD = "test.database.password";
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "test.hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "test.hibernate.show_sql";
	private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "test.entitymanager.packages.to.scan";
	
	/**********************Data Source Initialize**************************/
	
	@Value("classpath:db-schema.sql")
	private Resource schemaScript;

	//@Value("classpath:db-test-data.sql")
	//private Resource dataScript;

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
	    final DataSourceInitializer initializer = new DataSourceInitializer();
	    initializer.setDataSource(dataSource);
	    initializer.setDatabasePopulator(databasePopulator());
	    return initializer;
	}

	private DatabasePopulator databasePopulator() {
	    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	    populator.addScript(schemaScript);
	    //populator.addScript(dataScript);
	    return populator;
	}
	
	/*********************************************/

	// must give a name=dataSource to the data source bean
	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(env
				.getRequiredProperty(PROPERTY_NAME_DB_DRIVER));
		source.setUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
		source.setUsername(env.getRequiredProperty(PROPERTY_NAME_DB_USER));
		source.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));
		return source;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor createExcpetionTpp() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public JpaVendorAdapter getVendorAdapter() {

		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(getDataSource());

		entityManagerFactoryBean.setJpaVendorAdapter(getVendorAdapter());

		entityManagerFactoryBean
				.setPackagesToScan(env
						.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));

		entityManagerFactoryBean.setJpaProperties(hibProperties());

		return entityManagerFactoryBean;

	}

	private Properties hibProperties() {

		Properties properties = new Properties();

		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));

		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));

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
