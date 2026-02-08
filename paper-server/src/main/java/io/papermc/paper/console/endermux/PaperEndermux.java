package io.papermc.paper.console.endermux;

import java.nio.file.Path;
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

    public static @Nullable PaperEndermux earlyInstance;

    private @Nullable EndermuxServer endermuxServer;

    public void start(final Path socketPath) {
        Objects.requireNonNull(EndermuxForwardingAppender.INSTANCE);
        this.endermuxServer = new EndermuxServer(
            EndermuxForwardingAppender.INSTANCE.logLayout(),
            socketPath,
            5
        );
        this.endermuxServer.start();
        EndermuxForwardingAppender.TARGET = new RemoteLogForwarder(this.endermuxServer);
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
    }

}
