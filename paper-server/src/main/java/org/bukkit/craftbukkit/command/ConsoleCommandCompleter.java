package org.bukkit.craftbukkit.command;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;

// Paper start - JLine update
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
// Paper end
import org.bukkit.event.server.TabCompleteEvent;

public class ConsoleCommandCompleter implements Completer {
    private final DedicatedServer server; // Paper - CraftServer -> DedicatedServer
    private final io.papermc.paper.console.BrigadierCommandCompleter brigadierCompleter; // Paper - Enhance console tab completions for brigadier commands

    public ConsoleCommandCompleter(DedicatedServer server) { // Paper - CraftServer -> DedicatedServer
        this.server = server;
        this.brigadierCompleter = new io.papermc.paper.console.BrigadierCommandCompleter(this.server); // Paper - Enhance console tab completions for brigadier commands
    }

    // Paper start - Change method signature for JLine update
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        final CraftServer server = this.server.server;
        final String buffer = "/" + line.line();
        // Async Tab Complete
        final com.destroystokyo.paper.event.server.AsyncTabCompleteEvent event =
            new com.destroystokyo.paper.event.server.AsyncTabCompleteEvent(server.getConsoleSender(), buffer, true, null);
        event.callEvent();
        final List<com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion> completions = event.isCancelled() ? com.google.common.collect.ImmutableList.of() : event.completions();

        if (event.isCancelled() || event.isHandled()) {
            // Still fire sync event with the provided completions, if someone is listening
            if (!event.isCancelled() && TabCompleteEvent.getHandlerList().getRegisteredListeners().length > 0) {
                List<com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion> finalCompletions = new java.util.ArrayList<>(completions);
                Waitable<List<String>> syncCompletions = new Waitable<List<String>>() {
                    @Override
                    protected List<String> evaluate() {
                        org.bukkit.event.server.TabCompleteEvent syncEvent = new org.bukkit.event.server.TabCompleteEvent(server.getConsoleSender(), buffer,
                            finalCompletions.stream()
                                .map(com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion::suggestion)
                                .collect(java.util.stream.Collectors.toList()));
                        return syncEvent.callEvent() ? syncEvent.getCompletions() : com.google.common.collect.ImmutableList.of();
                    }
                };
                server.getServer().processQueue.add(syncCompletions);
                try {
                    final List<String> legacyCompletions = syncCompletions.get();
                    completions.removeIf(it -> !legacyCompletions.contains(it.suggestion())); // remove any suggestions that were removed
                    // add any new suggestions
                    for (final String completion : legacyCompletions) {
                        if (notNewSuggestion(completions, completion)) {
                            continue;
                        }
                        completions.add(com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion.completion(completion));
                    }
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }

            if (false && !completions.isEmpty()) {
                for (final com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion completion : completions) {
                    if (completion.suggestion().isEmpty()) {
                        continue;
                    }
                    candidates.add(new Candidate(
                        completion.suggestion(),
                        completion.suggestion(),
                        null,
                        io.papermc.paper.adventure.PaperAdventure.PLAIN.serializeOr(completion.tooltip(), null),
                        null,
                        null,
                        false
                    ));
                }
            }
            this.addCompletions(reader, line, candidates, completions);
            return;
        }

        // Paper end
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                List<String> offers = server.getCommandMap().tabComplete(server.getConsoleSender(), buffer); // Paper - Remove "this."

                TabCompleteEvent tabEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, (offers == null) ? Collections.EMPTY_LIST : offers); // Paper - Remove "this."
                server.getPluginManager().callEvent(tabEvent); // Paper - Remove "this."

                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        server.getServer().processQueue.add(waitable); // Paper - Remove "this."
        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                this.addCompletions(reader, line, candidates, Collections.emptyList()); // Paper - Enhance console tab completions for brigadier commands
                return; // Paper - Method returns void
            }

            // Paper start - JLine update
            /*
            for (String completion : offers) {
                if (completion.isEmpty()) {
                    continue;
                }

                candidates.add(new Candidate(completion));
            }
             */
            this.addCompletions(reader, line, candidates, offers.stream().map(com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion::completion).collect(java.util.stream.Collectors.toList()));
            // Paper end

            // Paper start - JLine handles cursor now
            /*
            final int lastSpace = buffer.lastIndexOf(' ');
            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
            */
            // Paper end
        } catch (ExecutionException e) {
            server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e); // Paper - Remove "this."
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Paper start
    private boolean notNewSuggestion(final List<com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion> completions, final String completion) {
        for (final com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion it : completions) {
            if (it.suggestion().equals(completion)) {
                return true;
            }
        }
        return false;
    }

    private void addCompletions(final LineReader reader, final ParsedLine line, final List<Candidate> candidates, final List<com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion> existing) {
        this.brigadierCompleter.complete(reader, line, candidates, existing);
    }
    // Paper end
}
