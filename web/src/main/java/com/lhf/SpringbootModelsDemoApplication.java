package com.lhf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringbootModelsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootModelsDemoApplication.class, args);
	}
}
