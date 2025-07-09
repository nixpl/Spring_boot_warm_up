package com.example.demo.sort.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractSortArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private final Map<String, String> sortAliasMap;
    private final Class<? extends Annotation> supportedAnnotation;

    public AbstractSortArgumentResolver(Map<String, String> sortAliasMap, Class<? extends Annotation> supportedAnnotation) {
        this.sortAliasMap = sortAliasMap;
        this.supportedAnnotation = supportedAnnotation;
    }

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(supportedAnnotation) && Pageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(
            @NonNull MethodParameter methodParameter,
            @NonNull ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            @NonNull WebDataBinderFactory binderFactory
    ) {
        log.info("Resolving Pageable argument.");
        Pageable initialPageable = (Pageable) super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        log.info("Initial Pageable resolved by parent: {}", initialPageable);

        List<Sort.Order> mappedOrders = new ArrayList<>();
        for (Sort.Order order : initialPageable.getSort()) {
            String property = order.getProperty();
            if (sortAliasMap.containsKey(property)) {
                String mappedProperty = sortAliasMap.get(property);
                log.info("Mapping sort alias '{}' to '{}'", property, mappedProperty);
                mappedOrders.add(new Sort.Order(order.getDirection(), mappedProperty));
            } else {
                log.info("No sort alias found for property '{}', using as is.", property);
                mappedOrders.add(order);
            }
        }

        Pageable finalPageable = PageRequest.of(
                initialPageable.getPageNumber(),
                initialPageable.getPageSize(),
                Sort.by(mappedOrders)
        );

        log.info("Returning final Pageable object: {}", finalPageable);
        return finalPageable;
    }
}
