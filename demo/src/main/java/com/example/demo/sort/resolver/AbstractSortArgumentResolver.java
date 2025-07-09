package com.example.demo.sort.resolver;

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
        Pageable pageable = (Pageable) super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        List<Sort.Order> mappedOrders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            if (sortAliasMap.containsKey(property)) {
                mappedOrders.add(new Sort.Order(order.getDirection(), sortAliasMap.get(property)));
            } else {
                mappedOrders.add(order);
            }
        }
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(mappedOrders)
        );
    }
}
