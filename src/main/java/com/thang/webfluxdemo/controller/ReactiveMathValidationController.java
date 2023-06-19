package com.thang.webfluxdemo.controller;

import com.thang.webfluxdemo.dto.Response;
import com.thang.webfluxdemo.exception.InputValidationException;
import com.thang.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

    @Autowired
    ReactiveMathService reactiveMathService;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input) {
        if (input < 10 || input > 20) {
            throw new InputValidationException(input);
        }
        return this.reactiveMathService.findSquare(input);
    }

    @GetMapping("square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable int input) {
        return Mono.just(input)
                .handle((integer, responseSynchronousSink) -> {
                    if (integer >= 10 && integer <= 20)
                        responseSynchronousSink.next(integer);
                    else
                        responseSynchronousSink.error(new InputValidationException(integer));
                })
                .cast(Integer.class)
                .flatMap(i -> this.reactiveMathService.findSquare(i));
    }

    @GetMapping("square/{input}/assignment")
    public Mono<ResponseEntity<Response>> assignment(@PathVariable int input) {
        return Mono.just(input)
                .filter(i -> i >= 10 && i <= 20)
                .flatMap(i -> this.reactiveMathService.findSquare(i))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
