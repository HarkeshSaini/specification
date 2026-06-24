package com.specification.service.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.specification.service.constant.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ApplicationConfiguration {

	@Bean
	public BeanFactoryPostProcessor nettyNoUnsafeSystemPropertyConfigurer(Environment environment) {
		return beanFactory -> {
			String noUnsafe = environment.getProperty(ApplicationConstant.NETTY_NO_UNSAFE_CONFIG_KEY, "true");
			System.setProperty(ApplicationConstant.NETTY_NO_UNSAFE_SYSTEM_PROPERTY, noUnsafe);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		log.debug(ApplicationConstant.CREATING_BCRYPT_PASSWORD_ENCODER_BEAN);
		return new BCryptPasswordEncoder();
	}
}
