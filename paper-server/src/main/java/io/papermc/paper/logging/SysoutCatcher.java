package io.papermc.paper.logging;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class SysoutCatcher {
    private static final boolean SUPPRESS_NAGS = Boolean.getBoolean("io.papermc.paper.suppress.sout.nags");
    // Nanoseconds between nag at most; if interval is caught first, this is reset.
    // <= 0 for disabling.
    private static final long NAG_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(
        Long.getLong("io.papermc.paper.sout.nags.timeout", TimeUnit.MINUTES.toMillis(5L)));
    // Count since last nag; if timeout is first, this is reset.
    // <= 0 for disabling.
    private static final long NAG_INTERVAL = Long.getLong("io.papermc.paper.sout.nags.interval", 200L);

    // We don't particularly care about how correct this is at any given moment; let's do it on a best attempt basis.
    // The records are also pretty small, so let's just go for a size of 64 to start...
    //
    // Content: Plugin name => nag object
    //  Why plugin name?: This doesn't store a reference to the plugin; keeps the reload ability.
    //  Why not clean on reload?: Effort.
    private final ConcurrentMap<String, PluginNag> nagRecords = new ConcurrentHashMap<>(64);

    public SysoutCatcher() {
        System.setOut(new WrappedOutStream(System.out, Level.INFO, "[STDOUT] "));
        System.setErr(new WrappedOutStream(System.err, Level.SEVERE, "[STDERR] "));
    }

    private final class WrappedOutStream extends PrintStream {
        private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        private final Level level;
        private final String prefix;

        public WrappedOutStream(@NotNull final OutputStream out, final Level level, final String prefix) {
            super(out);
            this.level = level;
            this.prefix = prefix;
        }

        @Override
        public void println(@Nullable final String line) {
            final Class<?> clazz = STACK_WALKER.getCallerClass();
            try {
                final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(clazz);

                // Instead of just printing the message, send it to the plugin's logger
                plugin.getLogger().log(this.level, this.prefix + line);

                if (SysoutCatcher.SUPPRESS_NAGS) {
                    return;
                }
                if (SysoutCatcher.NAG_INTERVAL > 0 || SysoutCatcher.NAG_TIMEOUT > 0) {
                    final PluginNag nagRecord = SysoutCatcher.this.nagRecords.computeIfAbsent(plugin.getName(), k -> new PluginNag());
                    final boolean hasTimePassed = SysoutCatcher.NAG_TIMEOUT > 0
                        && (nagRecord.lastNagTimestamp == Long.MIN_VALUE
                        || nagRecord.lastNagTimestamp + SysoutCatcher.NAG_TIMEOUT <= System.nanoTime());
                    final boolean hasMessagesPassed = SysoutCatcher.NAG_INTERVAL > 0
                        && (nagRecord.messagesSinceNag == Long.MIN_VALUE
                        || ++nagRecord.messagesSinceNag >= SysoutCatcher.NAG_INTERVAL);
                    if (!hasMessagesPassed && !hasTimePassed) {
                        return;
                    }
                    nagRecord.lastNagTimestamp = System.nanoTime();
                    nagRecord.messagesSinceNag = 0;
                }
                Bukkit.getLogger().warning(
                    String.format("Nag author(s): '%s' of '%s' about their usage of System.out/err.print. "
                            + "Please use your plugin's logger instead (JavaPlugin#getLogger).",
                        plugin.getPluginMeta().getAuthors(),
                        plugin.getPluginMeta().getDisplayName())
                );
            } catch (final IllegalArgumentException | IllegalStateException e) {
                // If anything happens, the calling class doesn't exist, there is no JavaPlugin that "owns" the calling class, etc
                // Just print out normally, with some added information
                Bukkit.getLogger().log(this.level, String.format("%s[%s] %s", this.prefix, clazz.getName(), line));
            }
        }
    }

    private static class PluginNag {
        private long lastNagTimestamp = Long.MIN_VALUE;
        private long messagesSinceNag = Long.MIN_VALUE;
    }
}
