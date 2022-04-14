package com.kyuth.resilience4j.demo.controller;

import com.kyuth.resilience4j.demo.service.CircuitBreakerTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/typeC")
public class TypeCTestController {

    private final CircuitBreakerTestService typeCService;

    public TypeCTestController(@Qualifier("typeCService") CircuitBreakerTestService typeCService) {
        this.typeCService = typeCService;
    }

    @GetMapping("failure")
    public String failure() {
        return typeCService.failure();
    }

    @GetMapping("success")
    public String success() {
        return typeCService.success();
    }

    @GetMapping("successException")
    public String successException() {
        return typeCService.successException();
    }

    @GetMapping("ignore")
    public String ignore() {
        return typeCService.ignoreException();
    }

    @GetMapping("futureFailure")
    public CompletableFuture<String> futureFailure() {
        return typeCService.futureFailure();
    }

    @GetMapping("futureSuccess")
    public CompletableFuture<String> futureSuccess() {
        return typeCService.futureSuccess();
    }

    @GetMapping("futureTimeout")
    public CompletableFuture<String> futureTimeout() {
        return typeCService.futureTimeout();
    }

    @GetMapping("fallback")
    public String failureWithFallback() {
        return typeCService.failureWithFallback();
    }
}
