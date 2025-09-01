package ru.baskakov;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main {
    private static final int THREAD_POOL_SIZE = 10;
    private static final int TOTAL_TASKS = 100;

    public static void main(String[] args) throws InterruptedException {
        DataProcessor dataProcessor = new DataProcessor(THREAD_POOL_SIZE);

        System.out.println("Starting 100 tasks...");
        List<String> tasks = generateAndSubmitTasks(dataProcessor);
        System.out.println("All tasks submitted. Start to complete tasks.");

        while (dataProcessor.getCountOfActiveTasks() > 0) {
            int activeTasks = dataProcessor.getCountOfActiveTasks();
            System.out.println("Active tasks: " + activeTasks);
            Thread.sleep(100);
        }

        System.out.println("All tasks completed!");
        System.out.println("Final active tasks: " + dataProcessor.getCountOfActiveTasks());

        printTasksResult(tasks, dataProcessor);

        dataProcessor.shutdown();

    }

    public static List<String> generateAndSubmitTasks(DataProcessor dataProcessor) {
        List<String> tasks = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < TOTAL_TASKS; i++) {
            List<Integer> numbers = new ArrayList<>();
            int numsCount = random.nextInt(10) + 1;
            for (int j = 0; j < numsCount; j++) {
                numbers.add(random.nextInt(100));
            }

            String taskName = dataProcessor.submitTask(numbers);
            tasks.add(taskName);
        }

        return tasks;
    }

    public static void printTasksResult(List<String> tasks, DataProcessor dataProcessor) {
        int sum = 0;
        for (String task : tasks) {
            Optional<Integer> result = dataProcessor.getTaskResult(task);
            result.ifPresent(value -> {
                System.out.println(task + ": " + value);
            });
            sum += result.orElse(0);
        }

        System.out.println("Sum of all tasks: " + sum);
        System.out.println("Total tasks executed: " + tasks.size());
    }
}