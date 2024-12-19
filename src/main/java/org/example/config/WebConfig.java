package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有来源访问
        registry.addMapping("/**")
                .allowedOrigins("http://8.156.69.47", "http://localhost:63342", "http://localhost:63343")  // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH, OPTIONS")  // 允许的 HTTP 方法
                .allowedHeaders("*"); // 允许所有请求头
    }
}
