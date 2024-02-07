package com.bezkoder.spring.jpa.h2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig  implements WebMvcConfigurer {
    @Override
    @Bean
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autorise les requêtes cross-origin pour toutes les routes
                .allowedOrigins("http://localhost:5173/*") // URL du front-end React
                .allowedMethods("GET")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
