package com.cloudslip.facade.config;

import com.cloudslip.facade.core.CustomSimpleMongoRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableMongoRepositories(basePackages = "com.cloudslip.facade.repository", repositoryBaseClass = CustomSimpleMongoRepository.class)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("x-auth-token", "Content-Type")
                .exposedHeaders("Content-Type")
                .allowCredentials(false).maxAge(3600);
    }
}
