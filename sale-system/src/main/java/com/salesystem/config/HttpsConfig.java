package com.salesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * HTTPS 配置（加分项4）
 * 当 spring.profiles.active=ssl 时启用
 * 启动命令: mvnw spring-boot:run -Dspring-boot.run.profiles=ssl
 * 配置文件: application-ssl.yml（SSL证书keystore.p12已生成在resources目录）
 */
@Configuration
@Profile("ssl")
public class HttpsConfig {
    // SSL 配置在 application-ssl.yml 中
    // 证书 keystore.p12 已生成于 src/main/resources/
    // HTTP (8080) → HTTPS (8443) 自动跳转由 Spring Boot 自动处理
}
