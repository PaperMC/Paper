package io.papermc.paper.util;

import java.util.concurrent.RejectedExecutionException;

public class ServerStopRejectedExecutionException extends RejectedExecutionException {
    public ServerStopRejectedExecutionException() {
    }

    public ServerStopRejectedExecutionException(final String message) {
        super(message);
    }

    public ServerStopRejectedExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServerStopRejectedExecutionException(final Throwable cause) {
        super(cause);
    }
}
