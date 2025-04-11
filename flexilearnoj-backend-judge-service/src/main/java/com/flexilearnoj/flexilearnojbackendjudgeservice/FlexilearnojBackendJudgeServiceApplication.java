package com.flexilearnoj.flexilearnojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.flexilearnoj.flexilearnojbackendserviceclient.service"})
public class FlexilearnojBackendJudgeServiceApplication {

    public static void main(String[] args) {
//      初始化消息队列，先注释掉，改用 Bean 的方式初始化消息队列（InitRabbitMqBean.java）
//      InitRabbitMq.doInit();
        SpringApplication.run(FlexilearnojBackendJudgeServiceApplication.class, args);
    }
}
