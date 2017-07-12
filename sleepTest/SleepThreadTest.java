package projectName.sleepTest;
/**
在synchronized代码块里面因为有共享变量count,无论线程A休眠多久，所以始终是A代码块先执行；因为sleep占用锁，导致在synchronized代码块中始终同步执行；
output:
A线程执行中.count++前 
A线程执行中.count++后 
A线程执行中.count = 2
B线程执行中.count++前 
B线程执行中.count++后 
B线程执行中.count = 3
或者
A线程执行中.count++前 
A线程执行中.count++后 
B线程执行中.count++前 
A线程执行中.count = 2
B线程执行中.count++后 
B线程执行中.count = 3
**/
public class SleepThreadTest extends Thread {

    private static  Integer count = 1;
    private String name;

    public SleepThreadTest(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (count){
            System.out.println(this.name + "线程执行中.count++前 ");
            count++;
            System.out.println(this.name + "线程执行中.count++后 ");
        }

        System.out.println(this.name + "线程执行中.count = " + count);
    }

    public static void main(String[] args) {
        Thread threadA = new SleepThreadTest("A");
        Thread threadB = new SleepThreadTest("B");

        threadA.start();
        threadB.start();

        try {
            threadA.sleep(1000);
   
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
