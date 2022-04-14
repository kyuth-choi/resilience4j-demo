package com.kyuth.resilience4j.demo.service;

import java.util.concurrent.CompletableFuture;

public interface CircuitBreakerTestService {

    String failure();

    String failureWithFallback();

    String success();

    String successException();

    String ignoreException();

    CompletableFuture<String> futureSuccess();

    CompletableFuture<String> futureFailure();

    CompletableFuture<String> futureTimeout();

}
