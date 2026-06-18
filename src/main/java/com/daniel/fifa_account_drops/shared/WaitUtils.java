package com.daniel.fifa_account_drops.shared;

public final class WaitUtils {

    public static void waitSafely(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
