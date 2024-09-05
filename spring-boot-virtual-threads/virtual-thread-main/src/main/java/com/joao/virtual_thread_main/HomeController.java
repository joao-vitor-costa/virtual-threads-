package com.joao.virtual_thread_main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@Slf4j
@RestController
public class HomeController {

    private final RestClient restClient;

    public HomeController(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8085/").build();
    }

    @GetMapping("/block/{seconds}")
    public String home(@PathVariable Integer seconds) throws InterruptedException {
        ResponseEntity<Void> result = restClient.get()
                .uri("/block/{seconds}", seconds)
                .retrieve()
                .toBodilessEntity();

        log.info("{} on {}", result.getStatusCode(), Thread.currentThread());

        return Thread.currentThread().toString();
    }
}
