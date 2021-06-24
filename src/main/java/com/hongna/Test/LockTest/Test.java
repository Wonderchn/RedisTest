package com.hongna.Test.LockTest;

public class Test {
    /**
     *  synchronized修饰普通的方法
     *  只保证当前实例对象同步方法不会被同个实例对象异步调用
     * @param args
     */
    public static void main(String[] args) {
        Altername altername = new Altername();
        Altername altername2 = new Altername();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    altername.getname();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //测试两个相同对象能否共同获取当前线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    altername2.getname();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
class Altername{
    int id;

    public synchronized  static String getname() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
        return Thread.currentThread().getName();
    }
}