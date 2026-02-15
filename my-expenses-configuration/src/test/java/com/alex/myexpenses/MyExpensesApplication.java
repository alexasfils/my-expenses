package com.alex.myexpenses;

import java.security.Security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.dentrassi.crypto.pem.PemKeyStoreProvider;

@SpringBootApplication
@ComponentScan(basePackages = "com.alex.myexpenses")
@EnableJpaRepositories(basePackages = "com.alex.myexpenses.repository")
@EntityScan("com.alex.myexpenses.entity")
public class MyExpensesApplication {
	
	public static void main(String[] args) {
		Security.addProvider(new PemKeyStoreProvider());
		SpringApplication.run(MyExpensesApplication.class, args);
	}

}
