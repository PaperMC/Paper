package io.papermc.paper.console.endermux;

import net.minecraft.server.dedicated.DedicatedServer;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.endermux.server.api.InteractiveConsoleHooks;

@NullMarked
public final class PaperCommandExecutor implements InteractiveConsoleHooks.CommandExecutor {
    private final DedicatedServer server;

    public PaperCommandExecutor(final DedicatedServer server) {
        this.server = server;
    }

    @Override
    public void execute(final String command) {
        this.server.handleConsoleInput(command, this.server.createCommandSourceStack());
    }
}
