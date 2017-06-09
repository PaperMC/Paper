package com.destroystokyo.paper.console;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.bukkit.craftbukkit.command.ConsoleCommandCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public final class PaperConsole extends SimpleTerminalConsole {

    private final DedicatedServer server;

    public PaperConsole(DedicatedServer server) {
        this.server = server;
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder
                .appName("Paper")
                .variable(LineReader.HISTORY_FILE, java.nio.file.Paths.get(".console_history"))
                .completer(new ConsoleCommandCompleter(this.server))
        );
    }

    @Override
    protected boolean isRunning() {
        return !this.server.isStopped() && this.server.isRunning();
    }

    @Override
    protected void runCommand(String command) {
        this.server.handleConsoleInput(command, this.server.createCommandSourceStack());
    }

    @Override
    protected void shutdown() {
        this.server.halt(false);
    }

}
