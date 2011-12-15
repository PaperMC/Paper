package org.bukkit.plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The PluginLogger class is a modified {@link java.util.logging.Logger} that prepends all logging calls with the name of the
 * plugin doing the logging.
 *
 * The API for PluginLogger is exactly the same as {@link java.util.logging.Logger}.
 */
public class PluginLogger extends Logger {

    private String pluginName;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     * @param context
     */
    public PluginLogger(Plugin context) {
        super(context.getClass().getCanonicalName(), null);
        pluginName = "[" + context.getDescription().getName() + "] ";
        setParent(context.getServer().getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(pluginName + logRecord.getMessage());
        super.log(logRecord);
    }

}
