package com.hongna.Test.LockTest;

import java.util.HashMap;

public class Test2 {
    /**
     *  synchronized关键字要是修饰方法，就证明锁的是当前class类
     * @param args
     */
    public static void main(String[] args) {
        HashMap  hashMap = new HashMap();
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
