package com.thang.webfluxdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class CalculatorRouterConfig {
    @Autowired
    private CalculatorHandler calculatorHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelCalculatorRouter() {
        return RouterFunctions.route()
                .path("calculator", this::serverResponseRouterFunctionAssignment)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunctionAssignment() {
        return RouterFunctions.route()
                .GET("{num1}/{num2}", isOperation("+"), calculatorHandler::additionHandler)
                .GET("{num1}/{num2}", isOperation("-"), calculatorHandler::subtractionHandler)
                .GET("{num1}/{num2}", isOperation("*"), calculatorHandler::multiplicationHandler)
                .GET("{num1}/{num2}", isOperation("/"), calculatorHandler::divisionHandler)
                .GET("{num1}/{num2}", req -> ServerResponse.badRequest().bodyValue("OP should be +, -, *, /"))
                .build();
    }

    private RequestPredicate isOperation(String operator) {
        return RequestPredicates.headers(headers -> {
            return operator.equals(headers.asHttpHeaders()
                    .toSingleValueMap()
                    .get("OP"));
        });
    }
}
