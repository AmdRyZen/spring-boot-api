package com.mltt;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.mltt.mapper")
@EnableDubbo
@NacosConfigurationProperties(dataId = "spring-boot-api-dev.yml", autoRefreshed = true)
@EnableDiscoveryClient
@SpringBootApplication
public class MlttApplication {
	public static void main(String[] args) {
		SpringApplication.run(MlttApplication.class, args);
	}
}
