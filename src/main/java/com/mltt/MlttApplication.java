package com.mltt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


@EnableOpenApi
@MapperScan("com.mltt.mapper")
@SpringBootApplication
public class MlttApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext app =  SpringApplication.run(MlttApplication.class, args);
		System.out.println("app.getEnvironment() = " + app.getEnvironment());
	}
}
