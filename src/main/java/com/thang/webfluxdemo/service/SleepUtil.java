package com.thang.webfluxdemo.service;

public class SleepUtil {
    public static void sleepSeconds(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
