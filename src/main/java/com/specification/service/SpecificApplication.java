package com.specification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.specification.service.constant.ApplicationConstant;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableReactiveMongoRepositories
public class SpecificApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpecificApplication.class, args);
	}

}
