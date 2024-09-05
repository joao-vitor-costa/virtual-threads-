package com.joao.completable_future_with_virtual_threads_alternative;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Log4j2
@RestController
public class LearningController {

    private final LearningService learningService;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public LearningController(LearningService learningService, @Qualifier("applicationTaskExecutor") AsyncTaskExecutor asyncTaskExecutor) {
        this.learningService = learningService;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @GetMapping(value = "learning/virtualthreads/asynctaskexecutor/alternative")
    public ResponseEntity<HttpStatus> createThreadsUsingAsyncExec() {
        System.out.println("Async task executor using virtual threads !!!");
        long startDateTime = System.currentTimeMillis();

        List<CompletableFuture<String>> cfList = new ArrayList<>();
        IntStream.range(0, 1000).forEach(i ->
                cfList.add(asyncTaskExecutor.submitCompletable(learningService::getThreadInfo))
        );

        cfList.forEach(cf -> {
                    try {
                        System.out.println(cf.get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        //CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).join();

        long endDateTime = System.currentTimeMillis();
        System.out.println("Time taken in milliseconds : " + (endDateTime - startDateTime));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
