package com.destroystokyo.paper.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.EventRoute;

public final class LogFullPolicy implements AsyncQueueFullPolicy {

    /*
     * Prevents log calls being logged out of order when the log queue is full.
     */

    @Override
    public EventRoute getRoute(final long backgroundThreadId, final Level level) {
        return EventRoute.ENQUEUE;
    }
}
