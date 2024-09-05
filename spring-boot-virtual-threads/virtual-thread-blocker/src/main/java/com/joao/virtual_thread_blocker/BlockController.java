package com.joao.virtual_thread_blocker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BlockController {

    @GetMapping("/block/{seconds}")
    public void block(@PathVariable Integer seconds) throws InterruptedException {
        Thread.sleep( seconds * 1000);
        log.info("Blocking for {} seconds", seconds);
    }
}
