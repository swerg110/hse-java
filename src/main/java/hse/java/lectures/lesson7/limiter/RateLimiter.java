package hse.java.lectures.lesson7.limiter;

import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class RateLimiter {

    private final long windowNanos;
    private final int maxRequests;
    private final LinkedList<Long> stamps = new LinkedList<>();

    public RateLimiter(ChronoUnit unit, int maxRequests) {
        if (unit != ChronoUnit.SECONDS && unit != ChronoUnit.MINUTES) {
            throw new IllegalArgumentException("bad unit");
        }
        if (maxRequests < 1) {
            throw new IllegalArgumentException("bad max");
        }
        this.windowNanos = unit.getDuration().toNanos();
        this.maxRequests = maxRequests;
    }

    public synchronized boolean check() {
        long now = System.nanoTime();
        long border = now - windowNanos;
        while (!stamps.isEmpty() && stamps.getFirst() <= border) {
            stamps.removeFirst();
        }
        if (stamps.size() < maxRequests) {
            stamps.addLast(now);
            return true;
        }
        return false;
    }
}
