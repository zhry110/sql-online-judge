package com.core;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.BlockingQueue;

public class JudgeThread extends Thread {
    private BlockingQueue<Problem> problems;
    public JudgeThread(@NotNull BlockingQueue<Problem> problems)
    {
        this.problems = problems;
    }
    @Override
    public void run() {
        super.run();
        Problem problem = null;
        while (true)
        {
            try {
                problem = problems.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
