package com.br.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {"com.br.lojavirtual.model"})
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.br.lojavirtual.repository"})
@EnableTransactionManagement
public class LojavirtualApplication {
	
	
    public static void main(String[] args) {
    	
    	
    	/* $2a$10$t8wQ4WntYXJPTKdeHqJdseHJC3xrgfXN02LhoPDH46AvWA/g6lXuS  */
    	
		SpringApplication.run(LojavirtualApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}
