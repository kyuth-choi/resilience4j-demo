package com.kyuth.resilience4j.demo.service;

import com.kyuth.resilience4j.demo.exceptions.IgnoreException;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class TypeAService implements CircuitBreakerTestService {

    private static final String TYPE_A = "typeA";

    @Override
    @CircuitBreaker(name = TYPE_A)
    @Bulkhead(name = TYPE_A)
    @Retry(name = TYPE_A)
    public String failure() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    @Override
    @CircuitBreaker(name = TYPE_A)
    @Bulkhead(name = TYPE_A)
    public String failureWithFallback() {
        throw new IgnoreException("This exception is ignored by the CircuitBreaker of Type A");
    }

    @Override
    public String ignoreException() {
        throw new IgnoreException("This exception is ignored by the CircuitBreaker of Type A");
    }

    @Override
    @CircuitBreaker(name = TYPE_A)
    @Bulkhead(name = TYPE_A)
    @Retry(name = TYPE_A)
    public String success() {
        return "Hello World from Type A";
    }

    @Override
    @CircuitBreaker(name = TYPE_A)
    @Bulkhead(name = TYPE_A)
    public String successException() {
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "This is a remote client exception");
    }

    @Override
    @Bulkhead(name = TYPE_A, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_A)
    @CircuitBreaker(name = TYPE_A)
    @Retry(name = TYPE_A)
    public CompletableFuture<String> futureSuccess() {
        return CompletableFuture.completedFuture("Hello World from Type A");
    }

    @Override
    @Bulkhead(name = TYPE_A, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_A)
    @CircuitBreaker(name = TYPE_A)
    @Retry(name = TYPE_A)
    public CompletableFuture<String> futureFailure() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new IOException("BAM!"));
        return future;
    }

    @Override
    @Bulkhead(name = TYPE_A, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_A)
    @CircuitBreaker(name = TYPE_A, fallbackMethod = "futureFallback")
    public CompletableFuture<String> futureTimeout() {
        Try.run(() -> Thread.sleep(5000));
        return CompletableFuture.completedFuture("Hello World from Type A");
    }

    private String fallback(HttpServerErrorException ex) {
        return "Recovered HttpServerErrorException: " + ex.getMessage();
    }

    private String fallback(Exception ex) {
        return "Recovered: " + ex.toString();
    }

    private CompletableFuture<String> futureFallback(TimeoutException ex) {
        return CompletableFuture.completedFuture("Recovered specific TimeoutException: " + ex.toString());
    }

    private CompletableFuture<String> futureFallback(BulkheadFullException ex) {
        return CompletableFuture.completedFuture("Recovered specific BulkheadFullException: " + ex.toString());
    }

    private CompletableFuture<String> futureFallback(CallNotPermittedException ex) {
        return CompletableFuture.completedFuture("Recovered specific CallNotPermittedException: " + ex.toString());
    }


}
