package com.shodhai.contest.judge;

import com.shodhai.contest.config.JudgeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.*;

@Component
@Slf4j
public class SubmissionQueue {
    
    private final JudgeConfig judgeConfig;
    private final JudgeService judgeService;
    private final BlockingQueue<Long> queue;
    private final ExecutorService executorService;
    private volatile boolean running = false;
    
    public SubmissionQueue(JudgeConfig judgeConfig, JudgeService judgeService) {
        this.judgeConfig = judgeConfig;
        this.judgeService = judgeService;
        this.queue = new LinkedBlockingQueue<>(judgeConfig.getQueueCapacity());
        this.executorService = Executors.newFixedThreadPool(judgeConfig.getWorkerThreads());
    }
    
    @PostConstruct
    public void start() {
        running = true;
        log.info("Starting submission queue with {} worker threads", judgeConfig.getWorkerThreads());
        
        for (int i = 0; i < judgeConfig.getWorkerThreads(); i++) {
            final int workerId = i;
            executorService.submit(() -> processQueue(workerId));
        }
    }
    
    @PreDestroy
    public void stop() {
        running = false;
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Executor service did not terminate gracefully");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Submission queue stopped");
    }
    
    public boolean addSubmission(Long submissionId) {
        try {
            boolean added = queue.offer(submissionId, 5, TimeUnit.SECONDS);
            if (added) {
                log.info("Submission {} added to queue. Queue size: {}", submissionId, queue.size());
            } else {
                log.warn("Failed to add submission {} to queue - queue is full", submissionId);
            }
            return added;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while adding submission to queue", e);
            return false;
        }
    }
    
    private void processQueue(int workerId) {
        log.info("Worker {} started", workerId);
        
        while (running) {
            try {
                Long submissionId = queue.poll(1, TimeUnit.SECONDS);
                if (submissionId != null) {
                    log.info("Worker {} processing submission {}", workerId, submissionId);
                    judgeService.processSubmission(submissionId);
                    log.info("Worker {} completed submission {}", workerId, submissionId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Worker {} error processing submission", workerId, e);
            }
        }
        
        log.info("Worker {} stopped", workerId);
    }
    
    public int getQueueSize() {
        return queue.size();
    }
}

