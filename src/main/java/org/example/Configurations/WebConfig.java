package org.example.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/Users/mushtaqu/Desktop/DevelopmentPractice/TicTacToe/resources/static/images/**")
//                .addResourceLocations("file:/Users/mushtaqu/Desktop/DevelopmentPractice/TicTacToe/resources/static/images/");
//    }

    // Inject the path from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // When a request comes in for /images/**
//        registry.addResourceHandler("/images/**")
//                // Map it to the external directory
//                .addResourceLocations("file:" + uploadDir);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // The URL path you will use in your frontend/API calls
        String resourceHandler = "/uploads/images/**";

        // The absolute path to the directory on your computer where the files are stored
        // The "file:" prefix is essential!
        String resourceLocation = "file:" + uploadDir;

        registry.addResourceHandler(resourceHandler)
                .addResourceLocations(resourceLocation);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")   // only allow API endpoints
                        .allowedOrigins("http://localhost:5173", "https://yourdomain.com") // dev + prod frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("x-session-id", "Content-Type")
                        .allowCredentials(true);
            }
        };
    }
}
