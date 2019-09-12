package com.myagent.martin6699s;

/**
 * @author martin
 * @date 2019/9/11
 **/
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("开始------");
        TimeHelloWorld helloWorld = new TimeHelloWorld();
        for(int i = 0; i < 60; i++) {
            helloWorld.sayHello();
            helloWorld.sayHelloMartin();

            Thread.sleep(10000);
        }

        System.out.println("结束------");
    }
}
