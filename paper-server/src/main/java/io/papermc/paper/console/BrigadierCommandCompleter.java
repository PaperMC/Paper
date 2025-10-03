package io.papermc.paper.console;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestion;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.dedicated.DedicatedServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import static com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion.completion;

public final class BrigadierCommandCompleter {
    private final Supplier<CommandSourceStack> commandSourceStack;
    private final DedicatedServer server;

    public BrigadierCommandCompleter(final @NonNull DedicatedServer server) {
        this.server = server;
        this.commandSourceStack = Suppliers.memoize(this.server::createCommandSourceStack);
    }

    public void complete(final @NonNull LineReader reader, final @NonNull ParsedLine line, final @NonNull List<Candidate> candidates, final @NonNull List<Completion> existing) {
        //noinspection ConstantConditions
        if (this.server.overworld() == null) { // check if overworld is null, as worlds haven't been loaded yet
            return;
        } else if (!io.papermc.paper.configuration.GlobalConfiguration.get().console.enableBrigadierCompletions) {
            this.addCandidates(candidates, Collections.emptyList(), existing, new ParseContext(line.line(), 0));
            return;
        }
        final CommandDispatcher<CommandSourceStack> dispatcher = this.server.getCommands().getDispatcher();
        final ParseResults<CommandSourceStack> results = dispatcher.parse(new StringReader(line.line()), this.commandSourceStack.get());
        this.addCandidates(
            candidates,
            CompletableFuture.supplyAsync(() -> dispatcher.getCompletionSuggestions(results, line.cursor()), this.server::scheduleOnMain)
                .thenCompose(Function.identity())
                .join()
                .getList(),
            existing,
            new ParseContext(line.line(), results.getContext().findSuggestionContext(line.cursor()).startPos)
        );
    }

    private void addCandidates(
        final @NonNull List<Candidate> candidates,
        final @NonNull List<Suggestion> brigSuggestions,
        final @NonNull List<Completion> existing,
        final @NonNull ParseContext context
    ) {
        brigSuggestions.forEach(it -> {
            if (it.getText().isEmpty()) return;
            candidates.add(toCandidate(it, context));
        });
        for (final AsyncTabCompleteEvent.Completion completion : existing) {
            if (completion.suggestion().isEmpty() || brigSuggestions.stream().anyMatch(it -> it.getText().equals(completion.suggestion()))) {
                continue;
            }
            candidates.add(toCandidate(completion));
        }
    }

    private static Candidate toCandidate(final Suggestion suggestion, final @NonNull ParseContext context) {
        Component tooltip = null;
        if (suggestion.getTooltip() != null) {
            tooltip = PaperAdventure.asAdventure(ComponentUtils.fromMessage(suggestion.getTooltip()));
        }
        return toCandidate(context.line.substring(context.suggestionStart, suggestion.getRange().getStart()) + suggestion.getText(), tooltip);
    }

    private static @NonNull Candidate toCandidate(final @NonNull Completion completion) {
        return toCandidate(completion.suggestion(), completion.tooltip());
    }

    private static @NonNull Candidate toCandidate(final @NonNull String suggestionText, final @Nullable Component tooltip) {
        final String suggestionTooltip = PaperAdventure.ANSI_SERIALIZER.serializeOr(tooltip, null);
        //noinspection SpellCheckingInspection
        return new PaperCandidate(
            suggestionText,
            suggestionText,
            null,
            suggestionTooltip,
            null,
            null,
            /*
            in an ideal world, this would sometimes be true if the suggestion represented the final possible value for a word.
            Like for `/execute alig`, pressing enter on align would add a trailing space if this value was true. But not all
            suggestions should add spaces after, like `/execute as @`, accepting any suggestion here would be valid, but its also
            valid to have a `[` following the selector
             */
            false
        );
    }

    private static @NonNull Completion toCompletion(final @NonNull Suggestion suggestion) {
        if (suggestion.getTooltip() == null) {
            return completion(suggestion.getText());
        }
        return completion(suggestion.getText(), PaperAdventure.asAdventure(ComponentUtils.fromMessage(suggestion.getTooltip())));
    }

    private record ParseContext(String line, int suggestionStart) {
    }

    public static final class PaperCandidate extends Candidate {
        public PaperCandidate(final String value, final String display, final String group, final String descr, final String suffix, final String key, final boolean complete) {
            super(value, display, group, descr, suffix, key, complete);
        }
    }
}
