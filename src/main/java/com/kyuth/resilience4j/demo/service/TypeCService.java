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
public class TypeCService implements CircuitBreakerTestService {

    private static final String TYPE_C = "typeC"; //config instance 없기때문에 default 옵션 따라감

    @Override
    @CircuitBreaker(name = TYPE_C)
    @Bulkhead(name = TYPE_C)
    @Retry(name = TYPE_C)
    public String failure() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    @Override
    @CircuitBreaker(name = TYPE_C)
    @Bulkhead(name = TYPE_C)
    public String failureWithFallback() {
        throw new IgnoreException("This exception is ignored by the CircuitBreaker of Type C");
    }

    @Override
    public String ignoreException() {
        throw new IgnoreException("This exception is ignored by the CircuitBreaker of Type C");
    }

    @Override
    @CircuitBreaker(name = TYPE_C)
    @Bulkhead(name = TYPE_C)
    @Retry(name = TYPE_C)
    public String success() {
        return "Hello World from Type C";
    }

    @Override
    @CircuitBreaker(name = TYPE_C)
    @Bulkhead(name = TYPE_C)
    public String successException() {
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "This is a remote client exception");
    }

    @Override
    @Bulkhead(name = TYPE_C, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_C)
    @CircuitBreaker(name = TYPE_C)
    @Retry(name = TYPE_C)
    public CompletableFuture<String> futureSuccess() {
        return CompletableFuture.completedFuture("Hello World from Type C");
    }

    @Override
    @Bulkhead(name = TYPE_C, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_C)
    @CircuitBreaker(name = TYPE_C)
    @Retry(name = TYPE_C)
    public CompletableFuture<String> futureFailure() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new IOException("BAM!"));
        return future;
    }

    @Override
    @Bulkhead(name = TYPE_C, type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = TYPE_C)
    @CircuitBreaker(name = TYPE_C, fallbackMethod = "futureFallback")
    public CompletableFuture<String> futureTimeout() {
        Try.run(() -> Thread.sleep(5000));
        return CompletableFuture.completedFuture("Hello World from Type C");
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
