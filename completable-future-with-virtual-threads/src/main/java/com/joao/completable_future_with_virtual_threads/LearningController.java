package com.joao.completable_future_with_virtual_threads;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @GetMapping("learning/virtualthreads/completablefuture")
    public ResponseEntity<HttpStatus> createThreadsUsingCF(@RequestParam boolean virtualThreadsEnabled, @RequestParam boolean executorServiceEnabled) {
        System.out.println("Async processing using completable future !!!");
        long startDateTime = System.currentTimeMillis();
        List<CompletableFuture<String>> cfList = new ArrayList<>();

        if (executorServiceEnabled) {
            log.info("Using ExecutorService");
            ExecutorService executorService;
            if (virtualThreadsEnabled)
                executorService = Executors.newVirtualThreadPerTaskExecutor();
            else
                executorService = Executors.newFixedThreadPool(100);

            IntStream.range(0, 1000).forEach(i ->
                    cfList.add(CompletableFuture.supplyAsync(() -> {
                        try {
                            return learningService.getThreadInfo();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService))
            );
        } else {
            log.info("Without using ExecutorService");
            IntStream.range(0, 1000).forEach(i ->
                    cfList.add(CompletableFuture.supplyAsync(() -> {
                        try {
                            return learningService.getThreadInfo();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }))
            );

        }

        cfList.forEach(cf -> {
            try {
                System.out.println(cf.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        long endDateTime = System.currentTimeMillis();
        System.out.println("Time taken in milliseconds : " + (endDateTime - startDateTime));

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
