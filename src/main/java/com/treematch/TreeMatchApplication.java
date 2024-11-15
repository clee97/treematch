package com.treematch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {
	"com.treematch.config",
	"com.treematch.controller",
	"com.treematch.repository",
	"com.treematch.service",
	"com.treematch.advice",
})
public class TreeMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreeMatchApplication.class, args);
	}

}
