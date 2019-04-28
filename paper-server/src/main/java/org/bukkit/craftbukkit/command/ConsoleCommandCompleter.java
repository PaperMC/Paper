package org.bukkit.craftbukkit.command;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import jline.console.completer.Completer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.TabCompleteEvent;

public class ConsoleCommandCompleter implements Completer {
    private final CraftServer server;

    public ConsoleCommandCompleter(CraftServer server) {
        this.server = server;
    }

    @Override
    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                List<String> offers = server.getCommandMap().tabComplete(server.getConsoleSender(), buffer);

                TabCompleteEvent tabEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, (offers == null) ? Collections.EMPTY_LIST : offers);
                server.getPluginManager().callEvent(tabEvent);

                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        this.server.getServer().processQueue.add(waitable);
        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return cursor;
            }
            candidates.addAll(offers);

            final int lastSpace = buffer.lastIndexOf(' ');
            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
        } catch (ExecutionException e) {
            this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return cursor;
    }
}
