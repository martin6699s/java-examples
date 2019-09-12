package com.myagent.martin6699s;

/**
 * @author martin
 * @date 2019/9/9
 **/
public class TimeHelloWorld {

    public void sayHello() {
        try {
            Thread.sleep(2000);
            System.out.println("hello world!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sayHelloMartin() {
        try {
            Thread.sleep(1000);
            System.out.println("hello martin!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
