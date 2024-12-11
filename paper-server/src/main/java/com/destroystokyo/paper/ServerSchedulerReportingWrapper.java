package com.destroystokyo.paper;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.scheduler.CraftTask;
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerSchedulerException;

/**
 * Reporting wrapper to catch exceptions not natively
 */
public class ServerSchedulerReportingWrapper implements Runnable {

    private final CraftTask internalTask;

    public ServerSchedulerReportingWrapper(CraftTask internalTask) {
        this.internalTask = Preconditions.checkNotNull(internalTask, "internalTask");
    }

    @Override
    public void run() {
        try {
            internalTask.run();
        } catch (RuntimeException e) {
            internalTask.getOwner().getServer().getPluginManager().callEvent(
                    new ServerExceptionEvent(new ServerSchedulerException(e, internalTask))
            );
            throw e;
        } catch (Throwable t) {
            internalTask.getOwner().getServer().getPluginManager().callEvent(
                    new ServerExceptionEvent(new ServerSchedulerException(t, internalTask))
            ); //Do not rethrow, since it is not permitted with Runnable#run
        }
    }

    public CraftTask getInternalTask() {
        return internalTask;
    }
}
