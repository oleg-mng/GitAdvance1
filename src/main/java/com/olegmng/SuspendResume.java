package com.olegmng;

/**
 * приостановка и возобновление потока исполнения современным способом
 */

class FirstThread implements Runnable{

    String name;
    Thread t;
    boolean suspendFlag;

    public FirstThread(String threadName) {
        this.name = threadName;
        t = new Thread(this, name);
        System.out.println("Новый поток" + t);
        suspendFlag = false;
        t.start();
    }

    @Override
    public void run() {
        try {
            for (int i = 15; i > 0; i--) {
                Thread.sleep(200);
                System.out.println(name + ": " + i);
                synchronized (this){
                    while (suspendFlag){
                        wait();
                    }
                }

            }
        } catch (InterruptedException e) {
            System.out.println(name + " прерван");
        }
        System.out.println(name + " завершен");

    }
    synchronized void mySuspend(){
        suspendFlag = true;
    }

    synchronized void myResume(){
        suspendFlag = false;
        notify();
    }
}

public class SuspendResume {
    public static void main(String[] args) {
        FirstThread ob1 = new FirstThread("Один");
        FirstThread ob2 = new FirstThread("Два");

        try {
            Thread.sleep(1000);
            ob1.mySuspend();
            System.out.println("Приостановка потока Один");
            Thread.sleep(1000);
            ob1.myResume();
            System.out.println("Возобновление потока Один");
            ob2.mySuspend();
            System.out.println("Приостановка потока Два");
            Thread.sleep(1000);
            ob2.myResume();
            System.out.println("Возобновление потока Два");
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");;
        }

        //ожидать завершения потоков исполнения
        try {
            System.out.println("ожидание завершения потоков");
            ob1.t.join();
            ob2.t.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");;
        }
        System.out.println("Главный поток завершен");
    }

}
