package com.joao.structured_concurrency_virtual_threads;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;


    @GetMapping(value = "learning/virtualthreads/structuredconcurrency")
    public ResponseEntity<HttpStatus> performStructuredConcurrencyUsingVirtualThreads() throws InterruptedException {
        System.out.println("Structured concurrency using virtual threads !!!");
        long startDateTime = System.currentTimeMillis();

        var scope = new StructuredTaskScope.ShutdownOnFailure();
        List<Supplier<String>> supplierList = new ArrayList<>();
        IntStream.range(0, 1000).forEach(i ->
                supplierList.add(scope.fork(() -> learningService.getThreadInfo()))
        );

        scope.join().throwIfFailed(RuntimeException::new);

        supplierList.forEach(supplier -> {
                    System.out.println(supplier.get());
                }
        );

        long endDateTime = System.currentTimeMillis();
        System.out.println("Time taken in milliseconds : " + (endDateTime - startDateTime));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
