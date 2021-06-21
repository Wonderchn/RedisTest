package com.hongna.Test.LockTest2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlternateDemo2 {
    public static void main(String[] args) {
        Alternate alternate = new Alternate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=20 ; i++){
                    alternate.showA(i);
                }

            }
        },"A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =1 ; i<= 20 ; i++){
                    alternate.showB(i);
                }

            }
        },"B").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i <= 20; i++){
                    alternate.showC(i);
                }

            }
        },"C").start();
    }

}
class Alternate{
    Lock lock = new ReentrantLock();
    int number = 1;
    Condition condition1 = lock.newCondition();
    Condition condition2  = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void showA(int totalLoop){
        lock.lock();
        try{
            if (number != 1){
                condition1.await();
            }
            for (int i = 0 ; i < 5 ; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 2;
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void showB(int totalLoop){
        lock.lock();
        try {
            if (number != 2){
                condition2.await();
            }
            for (int i = 0 ; i < 5 ; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            number = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void showC(int totalLoop){
        lock.lock();
        try {
            if (number != 3){
                condition3.await();
            }
            for (int i = 0 ; i < 5 ; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            number = 1;
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
