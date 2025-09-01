package ru.baskakov;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessor {
    private ExecutorService executor;
    private AtomicInteger taskCounter;
    private AtomicInteger activeTaskCounter;
    private Map<String, Integer> taskResults;

    public DataProcessor(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.taskCounter = new AtomicInteger(1);
        this.activeTaskCounter = new AtomicInteger(0);
        this.taskResults = new HashMap<>();
    }

    public String submitTask(List<Integer> nums) {
        String taskName = "task " + taskCounter.getAndIncrement();
        CalculateSumTask task = new CalculateSumTask(nums, taskName);
        activeTaskCounter.incrementAndGet();

        Future<Integer> future = executor.submit(() -> {
            try {
                Integer res = task.call();
                synchronized (taskResults) {
                    taskResults.put(taskName, res);
                }
                return res;
            } finally {
                activeTaskCounter.decrementAndGet();
            }
        });

        return taskName;
    }

    public int getCountOfActiveTasks() {
        return activeTaskCounter.get();
    }

    public Optional<Integer> getTaskResult(String taskName) {
        synchronized (taskResults) {
            return Optional.ofNullable(taskResults.get(taskName));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
