package com.hodbenor.project.eventsservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class SessionUserResolverConfig  implements WebMvcConfigurer {

    private final SessionUserArgumentResolver sessionUserArgumentResolver;

    public SessionUserResolverConfig(SessionUserArgumentResolver sessionUserArgumentResolver) {
        this.sessionUserArgumentResolver = sessionUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(sessionUserArgumentResolver);
    }
}
