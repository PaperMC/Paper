package io.papermc.paper.console;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.dedicated.DedicatedServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public final class BrigadierCommandHighlighter implements Highlighter {
    private static final int[] COLORS = {AttributedStyle.CYAN, AttributedStyle.YELLOW, AttributedStyle.GREEN, AttributedStyle.MAGENTA, /* Client uses GOLD here, not BLUE, however there is no GOLD AttributedStyle. */ AttributedStyle.BLUE};
    private final Supplier<CommandSourceStack> commandSourceStack;
    private final DedicatedServer server;

    public BrigadierCommandHighlighter(final @NonNull DedicatedServer server) {
        this.server = server;
        this.commandSourceStack = Suppliers.memoize(this.server::createCommandSourceStack);
    }

    @Override
    public AttributedString highlight(final @NonNull LineReader reader, final @NonNull String buffer) {
        //noinspection ConstantConditions
        if (this.server.overworld() == null) { // check if overworld is null, as worlds haven't been loaded yet
            return new AttributedString(buffer, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        }
        final AttributedStringBuilder builder = new AttributedStringBuilder();
        final ParseResults<CommandSourceStack> results = this.server.getCommands().getDispatcher().parse(new StringReader(buffer), this.commandSourceStack.get());
        int pos = 0;
        int component = -1;
        for (final ParsedCommandNode<CommandSourceStack> node : results.getContext().getLastChild().getNodes()) {
            if (node.getRange().getStart() >= buffer.length()) {
                break;
            }
            final int start = node.getRange().getStart();
            final int end = Math.min(node.getRange().getEnd(), buffer.length());
            builder.append(buffer.substring(pos, start), AttributedStyle.DEFAULT);
            if (node.getNode() instanceof LiteralCommandNode) {
                builder.append(buffer.substring(start, end), AttributedStyle.DEFAULT);
            } else {
                if (++component >= COLORS.length) {
                    component = 0;
                }
                builder.append(buffer.substring(start, end), AttributedStyle.DEFAULT.foreground(COLORS[component]));
            }
            pos = end;
        }
        if (pos < buffer.length()) {
            builder.append((buffer.substring(pos)), AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        }
        return builder.toAttributedString();
    }

    @Override
    public void setErrorPattern(final Pattern errorPattern) {}

    @Override
    public void setErrorIndex(final int errorIndex) {}
}
