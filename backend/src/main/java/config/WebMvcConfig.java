package com.example.storyworkshop.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.method.HandlerTypePredicate;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final String uploadRootPath;
    private final boolean standalone;

    public WebMvcConfig(@Value("${app.upload.root-path:../uploads}") String uploadRootPath,
                        @Value("${app.standalone:false}") boolean standalone) {
        this.uploadRootPath = uploadRootPath;
        this.standalone = standalone;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        if (standalone) {
            configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadRoot = Paths.get(uploadRootPath).toAbsolutePath().normalize();
        String resourceLocation = uploadRoot.toUri().toString();

        if (!resourceLocation.endsWith("/")) {
            resourceLocation = resourceLocation + "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
