package com.thang.webfluxdemo.service;

import com.thang.webfluxdemo.dto.MultiplyRequestDto;
import com.thang.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReactiveMathService {

    public Mono<Response> findSquare(int input) {
        return Mono.fromSupplier(() -> input * input)
                .map(Response::new);

    }

    public Flux<Response> multiplicationTable(int input) {

//        List<Response> list = IntStream.range(1,10)
//                .peek(i -> SleepUtil.sleepSeconds(1))
//                .peek(i -> System.out.println("math-service processing : " + i))
//                .mapToObj(i -> new Response(i*input))
//                .toList();
//        return Flux.fromIterable(list);

        return Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1))
//                .doOnNext(i -> SleepUtil.sleepSeconds(1))
                .doOnNext(i -> System.out.println("reactive-math-service processing : " + i))
                .map(i -> new Response(i * input));
    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
        return dtoMono
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(Response::new);
    }

    public Mono<Response> plus(int num1, int num2) {
        return Mono.fromSupplier(() -> num1 *  num2)
                .map(Response::new);
    }

    public Mono<Response> minus(int num1, int num2) {
        return Mono.fromSupplier(() -> num1 -  num2)
                .map(Response::new);
    }

    public Mono<Response> multiply(int num1, int num2) {
        return Mono.fromSupplier(() -> num1 *  num2)
                .map(Response::new);
    }

    public Mono<Response> divide(int num1, int num2) {
        return Mono.fromSupplier(() ->  num1 /  num2)
                .map(Response::new);
    }
}
