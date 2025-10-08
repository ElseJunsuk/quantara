package com.quant.quantara.external.util;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>Atlas 라이브러리의 유틸리티입니다.</p>
 * <p>
 * {@code Async} 클래스는 {@code CompletableFuture}를 활용하여 다수의 비동기 작업을
 * 효율적으로 실행할 수 있는 기능을 제공합니다.<br>
 * <br>
 * 이 클래스는 작업 당 처리량이 충분히 크거나 동시에 많은 작업을 실행할 때 유용하게 사용할 수
 * 있으며, 내부적으로 {@link ExecutorService}를 통해 비동기 작업을 처리합니다.<br>
 * <br>
 * <strong>참고로,</strong> 시스템의 가용 코어 수를 활용하여 스레드 풀 크기를 설정합니다.
 *
 * @author Else_JunSuk
 * @since 1.0
 */
public class Async {

    // 시스템의 가용 코어 수를 활용하여 스레드 풀 크기 설정
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     * 주어진 {@code Supplier} 작업을 {@code ExecutorService}에서 비동기적으로 실행하고,
     * 작업 결과를 {@code CompletableFuture} 형태로 반환합니다.<br>
     * <br>
     * 이 메소드는 단일 작업을 비동기적으로 처리할 때 유용하게 사용할 수 있습니다.
     *
     * @param supplier 비동기적으로 실행할 작업을 제공하는 {@code Supplier}
     * @param <T>      작업 결과 타입
     * @return 작업 결과를 포함하는 {@code CompletableFuture} 객체
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executorService);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    /**
     * 여러 개의 {@code Supplier} 작업을 비동기적으로 실행하고,
     * 모든 작업이 완료되면 각 작업의 결과들을 {@code List<T>} 형태로 반환하는
     * {@code CompletableFuture} 객체를 생성합니다.<br>
     * 개별 작업에서 발생한 예외는 명시적으로 처리하여, 전체 작업의 결과 수집 시
     * 예외가 발생한 경우 재처리하도록 합니다.
     *
     * @param taskList 비동기적으로 실행할 {@code Supplier} 작업들의 목록
     * @param <T>      작업 결과 타입
     * @return 모든 작업의 결과를 포함하는 {@code CompletableFuture} 객체
     */
    public static <T> CompletableFuture<List<T>> runAsyncTaskList(List<Supplier<T>> taskList) {
        List<CompletableFuture<T>> futureList = taskList.stream()
                .map(Async::supplyAsync)
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        return allOf.thenApply(v ->
                futureList.stream().map(future -> {
                    try {
                        return future.join();
                    } catch (CompletionException ex) {
                        // 개별 작업에서 발생한 예외를 감싸서 재처리
                        throw new RuntimeException("비동기 작업 중 예외 발생: " + ex.getCause().getMessage(), ex.getCause());
                    }
                }).collect(Collectors.toList())
        );
    }

    /**
     * 내부에서 사용 중인 {@code ExecutorService}를 종료합니다.
     * 먼저 {@code shutdown()}을 호출한 후, 일정 시간 동안 종료를 기다리고,
     * 종료되지 않은 경우 shutdownNow()를 호출하여 강제로 종료합니다.
     */
    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}