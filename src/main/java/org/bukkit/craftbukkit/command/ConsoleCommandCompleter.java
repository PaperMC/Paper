package org.bukkit.craftbukkit.command;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;

// Paper start - JLine update
import net.minecraft.server.DedicatedServer; // Paper
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
// Paper end
import org.bukkit.event.server.TabCompleteEvent;

public class ConsoleCommandCompleter implements Completer {
    private final DedicatedServer server; // Paper - CraftServer -> DedicatedServer

    public ConsoleCommandCompleter(DedicatedServer server) { // Paper - CraftServer -> DedicatedServer
        this.server = server;
    }

    // Paper start - Change method signature for JLine update
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        final CraftServer server = this.server.server;
        final String buffer = line.line();
        // Async Tab Complete
        com.destroystokyo.paper.event.server.AsyncTabCompleteEvent event;
        java.util.List<String> completions = new java.util.ArrayList<>();
        event = new com.destroystokyo.paper.event.server.AsyncTabCompleteEvent(server.getConsoleSender(), completions,
            buffer, true, null);
        event.callEvent();
        completions = event.isCancelled() ? com.google.common.collect.ImmutableList.of() : event.getCompletions();

        if (event.isCancelled() || event.isHandled()) {
            // Still fire sync event with the provided completions, if someone is listening
            if (!event.isCancelled() && TabCompleteEvent.getHandlerList().getRegisteredListeners().length > 0) {
                List<String> finalCompletions = completions;
                Waitable<List<String>> syncCompletions = new Waitable<List<String>>() {
                    @Override
                    protected List<String> evaluate() {
                        org.bukkit.event.server.TabCompleteEvent syncEvent = new org.bukkit.event.server.TabCompleteEvent(server.getConsoleSender(), buffer, finalCompletions);
                        return syncEvent.callEvent() ? syncEvent.getCompletions() : com.google.common.collect.ImmutableList.of();
                    }
                };
                server.getServer().processQueue.add(syncCompletions);
                try {
                    completions = syncCompletions.get();
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }

            if (!completions.isEmpty()) {
                candidates.addAll(completions.stream().map(Candidate::new).collect(java.util.stream.Collectors.toList()));
            }
            return;
        }

        // Paper end
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                List<String> offers = server.getCommandMap().tabComplete(server.getConsoleSender(), buffer);

                TabCompleteEvent tabEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, (offers == null) ? Collections.EMPTY_LIST : offers);
                server.getPluginManager().callEvent(tabEvent);

                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        server.getServer().processQueue.add(waitable); // Paper - Remove "this."
        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return; // Paper - Method returns void
            }

            // Paper start - JLine update
            for (String completion : offers) {
                if (completion.isEmpty()) {
                    continue;
                }

                candidates.add(new Candidate(completion));
            }
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
}
