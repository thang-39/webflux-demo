package com.thang.webfluxdemo.config;

import com.thang.webfluxdemo.dto.InputFailedValidationResponse;
import com.thang.webfluxdemo.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Autowired
    private CalculatorHandler calculatorHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter() {
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

//    @Bean
    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("square/{input}", request -> ServerResponse.badRequest().bodyValue("only 10-19 allowed"))
                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table/{input}/stream", requestHandler::tableStreamHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> assignmentHighLevel() {
        return RouterFunctions.route()
                .path("calculator", this::serverResponseRouterFunctionAssignment)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunctionAssignment() {
        return RouterFunctions.route()
                .GET("{num1}/{num2}", RequestPredicates.headers(headers -> Objects.equals(headers.firstHeader("OP"), "+")), calculatorHandler::plusHandlerAssignment)
                .GET("{num1}/{num2}", RequestPredicates.headers(headers -> Objects.equals(headers.firstHeader("OP"), "-")), calculatorHandler::minusHandlerAssignment)
                .GET("{num1}/{num2}", RequestPredicates.headers(headers -> Objects.equals(headers.firstHeader("OP"), "*")), calculatorHandler::multipleHandlerAssignment)
                .GET("{num1}/{num2}", RequestPredicates.headers(headers -> Objects.equals(headers.firstHeader("OP"), "/")), calculatorHandler::divideHandlerAssignment)
                .onError(ArithmeticException.class, exceptionHandlerArithmetic())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
            InputValidationException ex = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(ex.getInput());
            response.setMessage(ex.getMessage());
            response.setErrorCode(ex.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandlerArithmetic() {
        return (err, req) -> {
            ArithmeticException ex = (ArithmeticException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
//            response.setInput(ex.getInput());
            response.setMessage(ex.getMessage());
//            response.setErrorCode(ex.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }
}
