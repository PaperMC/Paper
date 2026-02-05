package io.papermc.paper.console.endermux;

import io.papermc.paper.console.BrigadierConsoleParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.craftbukkit.command.ConsoleCommandCompleter;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.terminal.TerminalBuilder;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.endermux.protocol.Payloads;
import xyz.jpenilla.endermux.server.api.ConsoleHooks;

@NullMarked
public final class PaperCommandCompleter implements ConsoleHooks.CommandCompleter {
    private final DedicatedServer server;
    private final LineReader lineReader;

    public PaperCommandCompleter(final DedicatedServer server) {
        this.server = server;
        try {
            this.lineReader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.builder().dumb(true).build())
                .build();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Payloads.CompletionResponse complete(final String command, final int cursor) {
        final ParsedLine parsedLine = new BrigadierConsoleParser(this.server).parse(command, cursor);
        final List<Candidate> candidates = new ArrayList<>();
        final ConsoleCommandCompleter completer = new ConsoleCommandCompleter(this.server);
        completer.complete(this.lineReader, parsedLine, candidates);

        final List<Payloads.CompletionResponse.CandidateInfo> candidateInfos = new ArrayList<>();
        for (final Candidate candidate : candidates) {
            candidateInfos.add(new Payloads.CompletionResponse.CandidateInfo(
                candidate.value(),
                candidate.displ(),
                candidate.descr()
            ));
        }

        return new Payloads.CompletionResponse(candidateInfos);
    }
}
