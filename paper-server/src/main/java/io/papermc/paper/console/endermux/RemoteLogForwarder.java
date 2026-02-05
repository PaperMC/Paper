package io.papermc.paper.console.endermux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import xyz.jpenilla.endermux.log4j.EndermuxForwardingAppender;
import xyz.jpenilla.endermux.protocol.Payloads;
import xyz.jpenilla.endermux.server.SocketServerManager;

@NullMarked
public final class RemoteLogForwarder implements EndermuxForwardingAppender.LogForwardingTarget {

    public static final String COMPONENT_LOG_MESSAGE_KEY = "component_log_message";

    private static final Logger LOGGER = LogManager.getLogger();

    private static final long ERROR_LOG_INTERVAL_MS = 60_000;

    private final SocketServerManager socketServerManager;
    private final AtomicLong failureCount = new AtomicLong(0);
    private volatile long lastErrorLogTime = 0;

    public RemoteLogForwarder(final SocketServerManager socketServerManager) {
        this.socketServerManager = socketServerManager;
    }

    @Override
    public void forward(final LogEvent event) {
        final SocketServerManager manager = this.socketServerManager;
        if (!manager.isRunning()) {
            return;
        }

        try {
            final String rawMessage = event.getMessage().getFormattedMessage();

            final Payloads.LogForward logPayload = new Payloads.LogForward(
                event.getLoggerName(),
                event.getLevel().toString(),
                rawMessage,
                event.getContextData().getValue(COMPONENT_LOG_MESSAGE_KEY),
                throwableInfo(event),
                event.getTimeMillis(),
                event.getThreadName()
            );

            manager.broadcastLog(logPayload);

        } catch (final Exception e) {
            this.handleForwardingError(e);
        }
    }

    private void handleForwardingError(final Exception e) {
        final long count = this.failureCount.incrementAndGet();
        final long now = System.currentTimeMillis();

        if (now - this.lastErrorLogTime > ERROR_LOG_INTERVAL_MS) {
            this.lastErrorLogTime = now;
            LOGGER.debug(
                "Failed to forward log event to socket clients (total failures: {}): {}",
                count,
                e.getMessage(),
                e
            );
        }
    }

    private static Payloads.@Nullable ThrowableInfo throwableInfo(final LogEvent event) {
        final Throwable thrown = event.getThrown();
        if (thrown != null) {
            return toThrowableInfo(thrown);
        }
        final ThrowableProxy proxy = event.getThrownProxy();
        return proxy == null ? null : toThrowableInfo(proxy);
    }

    private static Payloads.ThrowableInfo toThrowableInfo(final Throwable throwable) {
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        final Payloads.StackFrame[] frames = new Payloads.StackFrame[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++) {
            final StackTraceElement element = stackTrace[i];
            frames[i] = new Payloads.StackFrame(
                element.getClassName(),
                element.getMethodName(),
                element.getFileName(),
                element.getLineNumber()
            );
        }
        final Throwable[] suppressed = throwable.getSuppressed();
        final Payloads.ThrowableInfo[] suppressedInfo = new Payloads.ThrowableInfo[suppressed.length];
        for (int i = 0; i < suppressed.length; i++) {
            suppressedInfo[i] = toThrowableInfo(suppressed[i]);
        }
        return new Payloads.ThrowableInfo(
            throwable.getClass().getName(),
            throwable.getMessage(),
            Arrays.asList(frames),
            throwable.getCause() == null ? null : toThrowableInfo(throwable.getCause()),
            Arrays.asList(suppressedInfo)
        );
    }

    private static Payloads.ThrowableInfo toThrowableInfo(final ThrowableProxy proxy) {
        final ExtendedStackTraceElement[] stackTrace = proxy.getExtendedStackTrace();
        final Payloads.StackFrame[] frames = new Payloads.StackFrame[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++) {
            final StackTraceElement element = stackTrace[i].getStackTraceElement();
            frames[i] = new Payloads.StackFrame(
                element.getClassName(),
                element.getMethodName(),
                element.getFileName(),
                element.getLineNumber()
            );
        }
        final ThrowableProxy[] suppressed = proxy.getSuppressedProxies();
        final Payloads.ThrowableInfo[] suppressedInfo = suppressed == null
            ? new Payloads.ThrowableInfo[0]
            : new Payloads.ThrowableInfo[suppressed.length];
        if (suppressed != null) {
            for (int i = 0; i < suppressed.length; i++) {
                suppressedInfo[i] = toThrowableInfo(suppressed[i]);
            }
        }
        return new Payloads.ThrowableInfo(
            proxy.getName(),
            proxy.getMessage(),
            Arrays.asList(frames),
            proxy.getCauseProxy() == null ? null : toThrowableInfo(proxy.getCauseProxy()),
            Arrays.asList(suppressedInfo)
        );
    }
}
