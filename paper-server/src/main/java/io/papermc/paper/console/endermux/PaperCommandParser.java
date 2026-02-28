package io.papermc.paper.console.endermux;

import io.papermc.paper.console.BrigadierConsoleParser;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jline.reader.ParsedLine;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.endermux.protocol.Payloads;
import xyz.jpenilla.endermux.server.api.InteractiveConsoleHooks;

@NullMarked
public final class PaperCommandParser implements InteractiveConsoleHooks.CommandParser {
    private final DedicatedServer server;

    public PaperCommandParser(final DedicatedServer server) {
        this.server = server;
    }

    @Override
    public Payloads.ParseResponse parse(final String command, final int cursor) {
        final ParsedLine parsedLine = new BrigadierConsoleParser(this.server).parse(command, cursor);

        return new Payloads.ParseResponse(
            parsedLine.word(),
            parsedLine.wordCursor(),
            parsedLine.wordIndex(),
            parsedLine.words(),
            parsedLine.line(),
            parsedLine.cursor()
        );
    }
}
