package io.papermc.paper.console.endermux;

import java.nio.file.Path;
import java.util.Objects;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import xyz.jpenilla.endermux.log4j.EndermuxForwardingAppender;
import xyz.jpenilla.endermux.server.SocketServerManager;
import xyz.jpenilla.endermux.server.api.ConsoleHooks;
import xyz.jpenilla.endermux.server.log.RemoteLogForwarder;

@NullMarked
public final class PaperEndermux {

    private @Nullable SocketServerManager socketServerManager;

    public void start(final DedicatedServer server) {
        final Path socketPath = server.getServerDirectory().toFile().toPath().resolve("console.sock");
        Objects.requireNonNull(EndermuxForwardingAppender.INSTANCE);
        final ConsoleHooks hooks = ConsoleHooks.builder()
            .completer(new PaperCommandCompleter(server))
            .parser(new PaperCommandParser(server))
            .executor(new PaperCommandExecutor(server))
            .highlighter(new PaperCommandHighlighter(server))
            .metadata(new ConsoleHooks.Metadata(EndermuxForwardingAppender.INSTANCE.logLayout()))
            .build();
        this.socketServerManager = new SocketServerManager(hooks, socketPath, 5);
        this.socketServerManager.start();

        EndermuxForwardingAppender.TARGET = new RemoteLogForwarder(this.socketServerManager);
    }

    public void close() {
        if (this.socketServerManager != null) {
            this.socketServerManager.stop();
            this.socketServerManager = null;
        }

        EndermuxForwardingAppender.TARGET = null;
    }

}
