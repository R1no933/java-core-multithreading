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
        System.out.println(taskName + " is running in thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(200);
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            return sum;
        } catch (InterruptedException e) {
            System.out.println(taskName + " is interrupted: " + e.getMessage());
            throw e;
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public List<Integer> getNums() {
        return nums;
    }
}
