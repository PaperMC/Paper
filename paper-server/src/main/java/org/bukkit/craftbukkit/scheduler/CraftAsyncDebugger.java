package org.bukkit.craftbukkit.scheduler;

import org.bukkit.plugin.Plugin;


class CraftAsyncDebugger {
    private CraftAsyncDebugger next = null;
    private final int expiry;
    private final Plugin plugin;
    private final Class<? extends Runnable> clazz;

    CraftAsyncDebugger(final int expiry, final  Plugin plugin, final Class<? extends Runnable> clazz) {
        this.expiry = expiry;
        this.plugin = plugin;
        this.clazz = clazz;

    }

    final CraftAsyncDebugger getNextHead(final int time) {
        CraftAsyncDebugger next, current = this;
        while (time > current.expiry && (next = current.next) != null) {
            current = next;
        }
        return current;
    }

    final CraftAsyncDebugger setNext(final CraftAsyncDebugger next) {
        return this.next = next;
    }

    StringBuilder debugTo(final StringBuilder string) {
        for (CraftAsyncDebugger next = this; next != null; next = next.next) {
            string.append(plugin.getDescription().getName()).append(':').append(clazz.getName()).append('@').append(expiry).append(',');
        }
        return string;
    }
}
