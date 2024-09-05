package com.joao.completable_future_with_virtual_threads;

import org.springframework.stereotype.Service;

@Service
public class LearningService {

    public String getThreadInfo() throws InterruptedException {
        String threadInfo = "Entry Thread : " + Thread.currentThread();
        Thread.sleep(2000);
        threadInfo = threadInfo + ", Exit Thread : " + Thread.currentThread();
        return threadInfo;
    }
}
