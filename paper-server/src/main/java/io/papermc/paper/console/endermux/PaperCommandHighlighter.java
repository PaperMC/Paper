package io.papermc.paper.console.endermux;

import io.papermc.paper.console.BrigadierCommandHighlighter;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.endermux.protocol.Payloads;
import xyz.jpenilla.endermux.server.api.ConsoleHooks;

@NullMarked
public final class PaperCommandHighlighter implements ConsoleHooks.CommandHighlighter {
    private final DedicatedServer server;

    public PaperCommandHighlighter(final DedicatedServer server) {
        this.server = server;
    }

    @Override
    public Payloads.SyntaxHighlightResponse highlight(final String command) {
        return new Payloads.SyntaxHighlightResponse(command, this.highlightCommand(command));
    }

    private String highlightCommand(final String command) {
        try {
            return BrigadierCommandHighlighter.highlight(this.server, this.server::createCommandSourceStack, command).toAnsi();
        } catch (final Exception e) {
            final AttributedStringBuilder builder = new AttributedStringBuilder();
            builder.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
            builder.append(command);
            return builder.toAnsi();
        }
    }
}
