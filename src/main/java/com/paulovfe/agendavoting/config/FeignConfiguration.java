package com.paulovfe.agendavoting.config;

import com.paulovfe.agendavoting.integration.httpclient.FeignIntegrations;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = FeignIntegrations.class)
public class FeignConfiguration {
}