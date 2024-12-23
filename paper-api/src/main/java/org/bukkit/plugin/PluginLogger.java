package org.bukkit.plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The PluginLogger class is a modified {@link Logger} that prepends all
 * logging calls with the name of the plugin doing the logging. The API for
 * PluginLogger is exactly the same as {@link Logger}.
 *
 * @see Logger
 * @since 1.1.0
 */
public class PluginLogger extends Logger {
    private String pluginName;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public PluginLogger(@NotNull Plugin context) {
        super(context.getClass().getCanonicalName(), null);
        String prefix = context.getDescription().getPrefix();
        pluginName = prefix != null ? new StringBuilder().append("[").append(prefix).append("] ").toString() : "[" + context.getDescription().getName() + "] ";
        setParent(context.getServer().getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(@NotNull LogRecord logRecord) {
        logRecord.setMessage(pluginName + logRecord.getMessage());
        super.log(logRecord);
    }

}
