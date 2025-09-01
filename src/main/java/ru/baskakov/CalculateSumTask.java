package ru.baskakov;

import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {
    private List<Integer> nums;
    private String taskName;

    public CalculateSumTask(List<Integer> nums, String taskName) {
        this.nums = nums;
        this.taskName = taskName;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        System.out.println(taskName + " is running in thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(200);
            for (int i = 0; i < nums.size(); i++) {
                sum += nums.get(i);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return sum;
    }
}
