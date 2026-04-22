package com.filespark.client;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class LoginCooldown {

    private LoginCooldown() {}

    private static final AtomicInteger attemptCount = new AtomicInteger(0);
    private static final AtomicLong nextEnabledAt = new AtomicLong(0L);

    public static void recordAttempt() {

        int attempt = attemptCount.incrementAndGet();
        long delayMs = computeDelayMs(attempt);
        nextEnabledAt.set(System.currentTimeMillis() + delayMs);

    }

    public static long remainingMs() {

        long remaining = nextEnabledAt.get() - System.currentTimeMillis();
        return Math.max(remaining, 0L);

    }

    public static boolean isCoolingDown() {

        return remainingMs() > 0L;

    }

    public static void reset() {

        attemptCount.set(0);
        nextEnabledAt.set(0L);

    }

    // first click  → 0 seconds (free retry)
    // second click → 30s, then 1m, 2m, 4m, 8m, doubling thereafter
    private static long computeDelayMs(int attempt) {

        if (attempt <= 1) return 0L;
        long seconds = 30L * (1L << (attempt - 2));
        return seconds * 1000L;

    }

}
