package com.aigirlfriend.virtuallove.utils;

public class ClickUtil {
    private static final long INTERVAL = 500;
    private static long lastTime;

    public static boolean isTooFast() {
        long currentTimeMillis = System.currentTimeMillis() - lastTime;
        if (currentTimeMillis > 0 && currentTimeMillis < INTERVAL) {
            return true;
        }
        lastTime = System.currentTimeMillis();
        return false;
    }

    public static boolean isTooFast(long j) {
        long currentTimeMillis = System.currentTimeMillis() - lastTime;
        if (currentTimeMillis > 0 && currentTimeMillis < j) {
            return true;
        }
        lastTime = System.currentTimeMillis();
        return false;
    }

    public static boolean isTooFast(int i) {
        long currentTimeMillis = System.currentTimeMillis() - lastTime;
        if (currentTimeMillis > 0 && currentTimeMillis < ((long) i)) {
            return true;
        }
        lastTime = System.currentTimeMillis();
        return false;
    }
}
