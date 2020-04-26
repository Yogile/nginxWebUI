package com.cym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJdbcHttpSession
@SpringBootApplication
public class MongoStudyClient {

	public static void main(String[] args) {
		SpringApplication.run(MongoStudyClient.class, args);
	}
}
