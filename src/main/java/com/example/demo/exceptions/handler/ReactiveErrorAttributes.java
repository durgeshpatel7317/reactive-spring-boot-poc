package com.example.demo.exceptions.handler;

import com.example.demo.enums.ErrorAttributesKey;
import com.example.demo.exceptions.ProductNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ReactiveErrorAttributes extends DefaultErrorAttributes {
    private final List<ExceptionRule> exceptionsRules = List.of(
            new ExceptionRule(ProductNotFound.class, HttpStatus.BAD_REQUEST),
            new ExceptionRule(FileNotFoundException.class, HttpStatus.BAD_REQUEST)
    );

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        log.error("Error on path: {} - {} ==> {}: {} ", request.method(), request.path(), error.getClass().getName(), error.getMessage());

        Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
                .filter(exceptionRule -> exceptionRule.exceptionClass().isInstance(error))
                .findFirst();

        return exceptionRuleOptional.<Map<String, Object>>map(exceptionRule -> Map.of(ErrorAttributesKey.STATUS.getKey(), exceptionRule.status().value(), ErrorAttributesKey.MESSAGE.getKey(), error.getMessage()))
                .orElse(Map.of(ErrorAttributesKey.STATUS.getKey(), determineHttpStatus(error).value(),  ErrorAttributesKey.MESSAGE.getKey(), error.getMessage()));
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        return error instanceof ResponseStatusException err ? err.getStatus() :
                MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                        .get(ResponseStatus.class)
                        .getValue(ErrorAttributesKey.STATUS.getKey(), HttpStatus.class)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

record ExceptionRule(Class<?> exceptionClass, HttpStatus status){}
