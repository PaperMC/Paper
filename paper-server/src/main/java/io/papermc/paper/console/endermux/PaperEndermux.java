package io.papermc.paper.console.endermux;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import xyz.jpenilla.endermux.server.EndermuxServer;
import xyz.jpenilla.endermux.server.api.InteractiveConsoleHooks;
import xyz.jpenilla.endermux.server.log4j.EndermuxForwardingAppender;
import xyz.jpenilla.endermux.server.log4j.RemoteLogForwarder;

@NullMarked
public final class PaperEndermux {

    private static final String PROPERTY_PREFIX = "paper.endermux.";
    private static final boolean DEFAULT_ENABLED = false;
    private static final int DEFAULT_MAX_CONNECTIONS = 5;
    private static final String DEFAULT_SOCKET_PATH = "console.sock";

    public static final boolean ENABLED = Boolean.parseBoolean(System.getProperty(PROPERTY_PREFIX + "enabled", Boolean.toString(DEFAULT_ENABLED)));
    public static final int MAX_CONNECTIONS = readInt(PROPERTY_PREFIX + "maxConnections", DEFAULT_MAX_CONNECTIONS);
    public static final Path SOCKET_PATH = Paths.get(System.getProperty(PROPERTY_PREFIX + "socketPath", DEFAULT_SOCKET_PATH));

    public static @Nullable PaperEndermux INSTANCE;

    private @Nullable EndermuxServer endermuxServer;

    private static int readInt(final String key, final int defaultValue) {
        final String raw = System.getProperty(key);
        if (raw == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(raw);
        } catch (final NumberFormatException ignored) {
            return defaultValue;
        }
    }

    public void start() {
        Objects.requireNonNull(EndermuxForwardingAppender.INSTANCE);
        this.endermuxServer = new EndermuxServer(
            EndermuxForwardingAppender.INSTANCE.logLayout(),
            SOCKET_PATH,
            MAX_CONNECTIONS
        );
        EndermuxForwardingAppender.TARGET = new RemoteLogForwarder(this.endermuxServer);
        this.endermuxServer.start();
    }

    public void enableInteractivity(final DedicatedServer server) {
        if (this.endermuxServer != null) {
            this.endermuxServer.enableInteractivity(
                InteractiveConsoleHooks.builder()
                    .completer(new PaperCommandCompleter(server))
                    .parser(new PaperCommandParser(server))
                    .executor(new PaperCommandExecutor(server))
                    .highlighter(new PaperCommandHighlighter(server))
                    .build()
            );
        }
    }

    public void disableInteractivity() {
        if (this.endermuxServer != null) {
            this.endermuxServer.disableInteractivity();
        }
    }

    public void close() {
        if (this.endermuxServer != null) {
            this.endermuxServer.stop();
            this.endermuxServer = null;
        }

        EndermuxForwardingAppender.TARGET = null;
        INSTANCE = null;
    }

}
