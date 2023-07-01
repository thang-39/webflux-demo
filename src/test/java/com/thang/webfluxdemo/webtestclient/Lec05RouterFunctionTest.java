package com.thang.webfluxdemo.webtestclient;

import com.thang.webfluxdemo.config.CalculatorHandler;
import com.thang.webfluxdemo.config.RequestHandler;
import com.thang.webfluxdemo.config.RouterConfig;
import com.thang.webfluxdemo.dto.Response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;


@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {RouterConfig.class})
public class Lec05RouterFunctionTest {

    private WebTestClient client;

    @MockBean
    private RequestHandler handler;

    @MockBean
    private CalculatorHandler calculatorHandler;

//    @Autowired
//    private RouterConfig config;

    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    public void setClient() {
//        client = WebTestClient.bindToRouterFunction(config.highLevelRouter()).build();
        client = WebTestClient.bindToApplicationContext(ctx).build();

        // for test remote server...
//        WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
//                .get()
//                .uri()
//                ...
    }

    @Test
    public void test() {
        Mockito.when(handler.squareHandler(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue(new Response(225)));

        client
                .get()
                .uri("/router/square/{input}",15)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(225));
    }
}
