package com.hongna.Test.LockTest2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 虚假唤醒的解决：
 *  wait要始终保证在while循环当中。
 *
 *  我们发现使用if+else的操作，线程并不会关闭，而是在阻塞其中
 *  我们去除了else发现，这时候不该被唤醒的线程也被唤醒了(消费者A沉睡了，唤醒notifyAll ,没想到把消费者B唤醒了，而且并没有执行判断)
 *  由此我们可以的得出结论
 * ：
 * 在if块中使用wait方法，是非常危险的，因为一旦线程被唤醒，并得到锁，就不会再判断if条件，而执行if语句块外的代码，所以建议，凡是先要做条件判断，再wait的地方，都使用while循环来做。
 */
public class LockTest2 {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Producter producter = new Producter(clerk);
        Customer customer = new Customer(clerk);

        new Thread(producter,"生产者A").start();
        new Thread(customer,"消费者A").start();
        new Thread(producter,"生产者B").start();
        new Thread(customer,"消费者B").start();
    }
}

// 售货员
class Clerk {
    private int product = 0;
    private Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    // 进货
    public synchronized void add() {
        // 产品已满
        lock.lock();

        try{
            while (product >=1) {
                System.out.println(Thread.currentThread().getName() + ": " + "已满！");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }

            ++product;
            // 该线程从while中出来的时候，是满足条件的
            System.out.println(Thread.currentThread().getName() + ": " +"....................进货成功，剩下"+product);
            condition.signalAll();

        }finally {
            lock.unlock();
        }


    }

    // 卖货
    public  void sale() {
        lock.lock();
        try{

            while (product <=0) {
                System.out.println(Thread.currentThread().getName() + ": " + "没有买到货");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }

            --product;
            System.out.println(Thread.currentThread().getName() + ":买到了货物，剩下 " + product);
            condition.signalAll();
        }
       finally {
            lock.unlock();
        }

    }
}

// 生产者
class Producter implements Runnable {
    private Clerk clerk;

    public Producter(Clerk clerk) {
        this.clerk = clerk;
    }

    // 进货
    @Override
    public void run() {
        for(int i = 0; i < 20; ++i) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            clerk.add();
        }
    }
}

// 消费者
class Customer implements Runnable {
    private Clerk clerk;
    public Customer(Clerk clerk) {
        this.clerk = clerk;
    }

    // 买货
    @Override
    public void run() {
        for(int i = 0; i < 20; ++i) {
            clerk.sale();
        }
    }
}

