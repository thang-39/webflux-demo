package com.thang.webfluxdemo.config;

import com.thang.webfluxdemo.dto.Response;
import com.thang.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class CalculatorHandler {

    @Autowired
    private ReactiveMathService mathService;

    public Mono<ServerResponse> additionHandler(ServerRequest request) {
        return process(request, (a,b) -> ServerResponse.ok().bodyValue(a + b));
    }

    public Mono<ServerResponse> subtractionHandler(ServerRequest request) {
        return process(request, (a,b) -> ServerResponse.ok().bodyValue(a - b));
    }

    public Mono<ServerResponse> multiplicationHandler(ServerRequest request) {
        return process(request, (a,b) -> ServerResponse.ok().bodyValue(a * b));
    }

    public Mono<ServerResponse> divisionHandler(ServerRequest request) {
        return process(request, (a,b) -> {
            return b != 0 ? ServerResponse.ok().bodyValue(a / b) :
                    ServerResponse.badRequest().bodyValue("b can not be ZERO");
        });
    }

    private Mono<ServerResponse> process(ServerRequest request,
                                         BiFunction<Integer, Integer, Mono<ServerResponse>> opLogic) {
        int num1 = Integer.parseInt(request.pathVariable("num1"));
        int num2 = Integer.parseInt(request.pathVariable("num2"));
        return opLogic.apply(num1, num2);
    }


    public Mono<ServerResponse> plusHandlerAssignment(ServerRequest serverRequest) {
        int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
        int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));

        Mono<Response> responseMono = mathService.plus(num1,num2);
        return ServerResponse.ok().body(responseMono, Response.class);

    }

    public Mono<ServerResponse> minusHandlerAssignment(ServerRequest serverRequest) {
        int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
        int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));

        Mono<Response> responseMono = mathService.minus(num1,num2);
        return ServerResponse.ok().body(responseMono, Response.class);

    }

    public Mono<ServerResponse> multipleHandlerAssignment(ServerRequest serverRequest) {
        int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
        int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));

        Mono<Response> responseMono = mathService.multiply(num1,num2);
        return ServerResponse.ok().body(responseMono, Response.class);

    }

    public Mono<ServerResponse> divideHandlerAssignment(ServerRequest serverRequest) {
        int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
        int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));

        if (num2 == 0) {
            return Mono.error(ArithmeticException::new);
        }

        Mono<Response> responseMono = mathService.divide(num1,num2);
        return ServerResponse.ok().body(responseMono, Response.class);

    }


}
