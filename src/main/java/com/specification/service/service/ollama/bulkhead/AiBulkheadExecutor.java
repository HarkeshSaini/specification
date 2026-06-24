package com.specification.service.service.ollama.bulkhead;

import com.specification.service.config.AiBulkheadProperties;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Bulkhead: limits concurrent AI work using a dedicated thread pool and semaphore.
 */
@Slf4j
public class AiBulkheadExecutor {

    private final String name;
    private final Semaphore semaphore;
    private final Duration acquireTimeout;
    private final ExecutorService executorService;

    public AiBulkheadExecutor(String name, AiBulkheadProperties.PoolConfig config) {
        this.name = name;
        this.semaphore = new Semaphore(Math.max(1, config.getMaxConcurrent()), true);
        this.acquireTimeout = config.acquireTimeout();
        int maxConcurrent = Math.max(1, config.getMaxConcurrent());
        int queueCapacity = Math.max(1, config.getQueueCapacity());
        this.executorService = new ThreadPoolExecutor(
                maxConcurrent,
                maxConcurrent,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                namedThreadFactory(name),
                new ThreadPoolExecutor.AbortPolicy()
        );
        log.info("AI bulkhead '{}' initialized (maxConcurrent={}, queue={})", name, maxConcurrent, queueCapacity);
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            executorService.execute(() -> runWithPermit(task, future));
        } catch (RejectedExecutionException ex) {
            future.completeExceptionally(AiBulkheadFullException.forPool(name));
        }
        return future;
    }

    public CompletableFuture<Void> run(Runnable task) {
        return submit(() -> {
            task.run();
            return null;
        });
    }

    private <T> void runWithPermit(Supplier<T> task, CompletableFuture<T> future) {
        boolean acquired = false;
        try {
            acquired = semaphore.tryAcquire(acquireTimeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!acquired) {
                future.completeExceptionally(AiBulkheadFullException.forPool(name));
                return;
            }
            future.complete(task.get());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            future.completeExceptionally(new CompletionException(ex));
        } catch (Throwable ex) {
            future.completeExceptionally(ex);
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }

    private static ThreadFactory namedThreadFactory(String poolName) {
        AtomicInteger counter = new AtomicInteger();
        return runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(poolName + "-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
    }
}
