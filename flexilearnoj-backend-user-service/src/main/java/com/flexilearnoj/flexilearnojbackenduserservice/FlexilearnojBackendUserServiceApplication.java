package com.flexilearnoj.flexilearnojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.flexilearnoj.flexilearnojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.flexilearnoj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.flexilearnoj.flexilearnojbackendserviceclient.service"})
public class FlexilearnojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexilearnojBackendUserServiceApplication.class, args);
    }

}
