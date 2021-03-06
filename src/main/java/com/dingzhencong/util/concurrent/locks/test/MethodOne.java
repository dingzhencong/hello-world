package com.dingzhencong.util.concurrent.locks.test;

public class MethodOne {
    private final ThreadToGo threadToGo = new ThreadToGo();
    public Runnable newThreadOne() {
        final String[] inputArr = Helper.buildNoArr(52);
        return new Runnable() {
            private String[] arr = inputArr;
            public void run() {
                try {
                    for (int i = 0; i < inputArr.length; i=i+2) {
                        synchronized (threadToGo) {
                            while (threadToGo.value == 2)
                                threadToGo.wait();
                            Helper.print(inputArr[i], arr[i + 1]);
                            threadToGo.value = 2;
                            threadToGo.notify();
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Oops...");
                }
            }
        };
    }
    public Runnable newThreadTwo() {
        final String[] inputArr = Helper.buildCharArr(26);
        return () -> {
            try {
                for (int i = 0; i < inputArr.length; i++) {
                    synchronized (threadToGo) {
                        while (threadToGo.value == 1)
                            threadToGo.wait();
                        Helper.print(inputArr[i]);
                        threadToGo.value = 1;
                        threadToGo.notify();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Oops...");
            }
        };
    }
    class ThreadToGo {
        int value = 1;
    }
    public static void main(String args[]) throws InterruptedException {
        MethodOne one = new MethodOne();
        Helper.instance.run(one.newThreadOne());
        Helper.instance.run(one.newThreadTwo());
        Helper.instance.shutdown();
    }
}
