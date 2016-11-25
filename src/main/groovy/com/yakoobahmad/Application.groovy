package com.yakoobahmad

import org.grails.datastore.gorm.jdbc.DataSourceBuilder
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import static grails.async.Promises.*

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
class Application {

	static ApplicationContext context

	static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication()
		context = springApplication.run(Application.class,args)

		// initialize app
		init()


	}

	/**
	 * initialize application once our spring application context is avilable
	 */
	static void init(){

		task {
			context.getBean(AkkaService.class).init()
		}

	}

	/**
	 * configure primary datasource bean
	 * @return
	 */
	@Bean
	@Primary
	@ConfigurationProperties(prefix="dataSources.dataSource")
	public javax.sql.DataSource dataSource() {
		return DataSourceBuilder.create().build()
	}



}
