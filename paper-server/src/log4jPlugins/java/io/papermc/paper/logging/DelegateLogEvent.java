package io.papermc.paper.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.util.Map;

public class DelegateLogEvent implements LogEvent {
    private final LogEvent original;

    protected DelegateLogEvent(LogEvent original) {
        this.original = original;
    }

    @Override
    public LogEvent toImmutable() {
        return this.original.toImmutable();
    }

    @Override
    public Map<String, String> getContextMap() {
        return this.original.getContextMap();
    }

    @Override
    public ReadOnlyStringMap getContextData() {
        return this.original.getContextData();
    }

    @Override
    public ThreadContext.ContextStack getContextStack() {
        return this.original.getContextStack();
    }

    @Override
    public String getLoggerFqcn() {
        return this.original.getLoggerFqcn();
    }

    @Override
    public Level getLevel() {
        return this.original.getLevel();
    }

    @Override
    public String getLoggerName() {
        return this.original.getLoggerName();
    }

    @Override
    public Marker getMarker() {
        return this.original.getMarker();
    }

    @Override
    public Message getMessage() {
        return this.original.getMessage();
    }

    @Override
    public long getTimeMillis() {
        return this.original.getTimeMillis();
    }

    @Override
    public Instant getInstant() {
        return this.original.getInstant();
    }

    @Override
    public StackTraceElement getSource() {
        return this.original.getSource();
    }

    @Override
    public String getThreadName() {
        return this.original.getThreadName();
    }

    @Override
    public long getThreadId() {
        return this.original.getThreadId();
    }

    @Override
    public int getThreadPriority() {
        return this.original.getThreadPriority();
    }

    @Override
    public Throwable getThrown() {
        return this.original.getThrown();
    }

    @Override
    public ThrowableProxy getThrownProxy() {
        return this.original.getThrownProxy();
    }

    @Override
    public boolean isEndOfBatch() {
        return this.original.isEndOfBatch();
    }

    @Override
    public boolean isIncludeLocation() {
        return this.original.isIncludeLocation();
    }

    @Override
    public void setEndOfBatch(boolean endOfBatch) {
        this.original.setEndOfBatch(endOfBatch);
    }

    @Override
    public void setIncludeLocation(boolean locationRequired) {
        this.original.setIncludeLocation(locationRequired);
    }

    @Override
    public long getNanoTime() {
        return this.original.getNanoTime();
    }
}
