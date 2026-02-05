package io.papermc.paper.console.endermux;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.jspecify.annotations.NullMarked;
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
}
