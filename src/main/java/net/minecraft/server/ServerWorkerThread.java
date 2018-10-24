package net.minecraft.server;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerWorkerThread extends Thread {
    private static final AtomicInteger threadId = new AtomicInteger(1);
    public ServerWorkerThread(Runnable target, String poolName, int prioritityModifier) {
        super(target, "Worker-" + poolName + "-" + threadId.getAndIncrement());
        setPriority(Thread.NORM_PRIORITY+prioritityModifier); // Deprioritize over main
        this.setDaemon(true);
        this.setUncaughtExceptionHandler(SystemUtils::onThreadError);
    }
}
