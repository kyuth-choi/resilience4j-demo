package com.kyuth.resilience4j.demo.controller;

import com.kyuth.resilience4j.demo.service.CircuitBreakerTestService;
import com.kyuth.resilience4j.demo.service.TypeAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/typeA")
public class TypeATestController {

    private final CircuitBreakerTestService typeAService;

    public TypeATestController(@Qualifier("typeAService") CircuitBreakerTestService typeAService) {
        this.typeAService = typeAService;
    }

    @GetMapping("failure")
    public String failure() {
        return typeAService.failure();
    }

    @GetMapping("success")
    public String success() {
        return typeAService.success();
    }

    @GetMapping("successException")
    public String successException() {
        return typeAService.successException();
    }

    @GetMapping("ignore")
    public String ignore() {
        return typeAService.ignoreException();
    }

    @GetMapping("futureFailure")
    public CompletableFuture<String> futureFailure() {
        return typeAService.futureFailure();
    }

    @GetMapping("futureSuccess")
    public CompletableFuture<String> futureSuccess() {
        return typeAService.futureSuccess();
    }

    @GetMapping("futureTimeout")
    public CompletableFuture<String> futureTimeout() {
        return typeAService.futureTimeout();
    }

    @GetMapping("fallback")
    public String failureWithFallback() {
        return typeAService.failureWithFallback();
    }
}
