package com.core;



import java.util.concurrent.BlockingQueue;

public class JudgeThread extends Thread {
    private BlockingQueue<Problem> problems;
    public JudgeThread(BlockingQueue<Problem> problems)
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
