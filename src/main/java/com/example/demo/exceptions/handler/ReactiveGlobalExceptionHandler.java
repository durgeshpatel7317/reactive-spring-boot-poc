package com.example.demo.exceptions.handler;

import com.example.demo.enums.ErrorAttributesKey;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ReactiveGlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    public ReactiveGlobalExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                          ServerCodecConfigurer configurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }

    private Mono<ServerResponse> renderException(ServerRequest request) {
        final Map<String, Object> errorAttributes = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());

        int statusCode = Integer.parseInt(errorAttributes.get(ErrorAttributesKey.STATUS.getKey()).toString());
        return ServerResponse.status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributes));
    }
}
