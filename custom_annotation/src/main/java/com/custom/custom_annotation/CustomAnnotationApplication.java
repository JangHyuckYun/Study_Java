package com.custom.custom_annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CustomAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomAnnotationApplication.class, args);

	}

}
