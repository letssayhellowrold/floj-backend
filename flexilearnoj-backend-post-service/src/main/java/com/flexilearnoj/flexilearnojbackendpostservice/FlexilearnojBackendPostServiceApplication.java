package com.flexilearnoj.flexilearnojbackendpostservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.flexilearnoj.flexilearnojbackendpostservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.flexilearnoj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.flexilearnoj.flexilearnojbackendserviceclient.service"})
public class FlexilearnojBackendPostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexilearnojBackendPostServiceApplication.class, args);
    }

}
