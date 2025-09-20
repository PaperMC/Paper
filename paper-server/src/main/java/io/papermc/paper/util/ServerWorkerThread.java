package io.papermc.paper.util;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.Util;

public class ServerWorkerThread extends Thread {
    private static final AtomicInteger threadId = new AtomicInteger(1);
    public ServerWorkerThread(Runnable target, String poolName, int priorityModifier) {
        super(target, "Worker-" + poolName + "-" + threadId.getAndIncrement());
        setPriority(Thread.NORM_PRIORITY + priorityModifier); // Deprioritize over main
        this.setDaemon(true);
        this.setUncaughtExceptionHandler(Util::onThreadException);
    }
}
