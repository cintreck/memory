package com.codex.memory;

/**
 * Tracks elapsed time between automatic screenshots.
 */
public final class MemoryClientScreenshotTimer {
    private final long intervalMillis;
    private long lastCaptureMillis;

    public MemoryClientScreenshotTimer(long intervalMillis) {
        this.intervalMillis = intervalMillis;
        this.lastCaptureMillis = System.currentTimeMillis();
    }

    public boolean shouldCapture(long nowMillis) {
        return nowMillis - lastCaptureMillis >= intervalMillis;
    }

    public void markCaptured(long nowMillis) {
        lastCaptureMillis = nowMillis;
    }

    public void reset(long nowMillis) {
        lastCaptureMillis = nowMillis;
    }
}
