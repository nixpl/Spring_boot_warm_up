package com.example.demo.config;

import com.example.demo.sort.resolver.AddressSortArgumentResolver;
import com.example.demo.sort.resolver.CitySortArgumentResolver;
import com.example.demo.sort.resolver.CustomerSortArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(0, new CustomerSortArgumentResolver());
        resolvers.add(1, new AddressSortArgumentResolver());
        resolvers.add(2, new CitySortArgumentResolver());
    }
}