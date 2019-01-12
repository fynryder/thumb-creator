package com.processor.db;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DBAccessor {
	
	private static DBAccessor instance = new DBAccessor();
	private SessionFactory sessionFactoryObj;
	
	 public DBAccessor() {
		Configuration config = new Configuration().addProperties(setProperties());
		config.addAnnotatedClass(com.processor.Entities.ProcessedImage.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				config.getProperties()).build();
		sessionFactoryObj = config.buildSessionFactory(serviceRegistry);
		
	}
	
	private Properties setProperties() {
		Properties prop= new Properties();

        prop.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/ImageProcessor");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");

        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.connection.password", "Mukesh@123");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        prop.setProperty("hibernate.c3p0.min_size","1");
        prop.setProperty("hibernate.c3p0.max_size","2");
        prop.setProperty("hibernate.c3p0.timeout","300");
        prop.setProperty("hibernate.c3p0.max_statements","50");
        prop.setProperty("hibernate.c3p0.idle_test_period","3000");
        prop.setProperty("hibernate.connection.provider_class","org.hibernate.connection.C3P0ConnectionProvider");
        
        return prop;
	}
	
	public static DBAccessor getInstance() {
		return instance;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactoryObj;
	}
	
}
