package com.destroystokyo.paper.console;

import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.console.BrigadierCompletionMatcher;
import io.papermc.paper.console.BrigadierConsoleParser;
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
        builder
                .appName("Paper")
                .variable(LineReader.HISTORY_FILE, java.nio.file.Paths.get(".console_history"))
                .completer(new ConsoleCommandCompleter(this.server))
                .option(LineReader.Option.COMPLETE_IN_WORD, true);
        if (io.papermc.paper.configuration.GlobalConfiguration.get().console.enableBrigadierHighlighting) {
            builder.highlighter(new io.papermc.paper.console.BrigadierCommandHighlighter(this.server));
        }
        if (GlobalConfiguration.get().console.enableBrigadierCompletions) {
            System.setProperty("org.jline.reader.support.parsedline", "true"); // to hide a warning message about the parser not supporting
            builder.parser(new BrigadierConsoleParser(this.server));
            builder.completionMatcher(new BrigadierCompletionMatcher());
        }
        return super.buildReader(builder);
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
