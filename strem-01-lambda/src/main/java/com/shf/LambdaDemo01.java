package com.shf;

public class LambdaDemo01 {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("新线程中run方法被执行了");
            }
        }).start();

        new Thread(()-> System.out.println("新线程中run方法被执行了")).start();
    }
}
