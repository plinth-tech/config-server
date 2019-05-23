package tech.plinth.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.plinth.config.interceptor.ConfigureRequestContext;

import javax.annotation.Resource;

@SpringBootApplication
public class Bootstrap implements WebMvcConfigurer {
    @Resource
    private ConfigureRequestContext requestContextInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor).addPathPatterns("/**");
    }
}