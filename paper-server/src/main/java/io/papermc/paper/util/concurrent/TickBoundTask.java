package io.papermc.paper.util.concurrent;

public interface TickBoundTask {
    long getNextRun();
    void setNextRun(long next);
    long getCreatedAt();
}
