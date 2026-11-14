package com.destroystokyo.paper.utils;

import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Prevents plugins (e.g. Essentials) from changing the parent of the plugin logger.
 */
public final class PaperPluginLogger extends Logger {

    @Deprecated(forRemoval = true)
    public static Logger getLogger(final PluginDescriptionFile description) {
        return getLogger((PluginMeta) description);
    }

    /**
     * Creates a logger for the given plugin and registers it with the {@link LogManager}.
     * <p>
     * If a logger with the same name is already registered, that logger is returned instead.
     *
     * @param meta the meta of the plugin to create the logger for
     * @return the logger for the plugin
     */
    public static Logger getLogger(final PluginMeta meta) {
        Logger logger = new PaperPluginLogger(meta);
        if (!LogManager.getLogManager().addLogger(logger)) {
            // Disable this if it's going to happen across reloads anyways...
            //logger.log(Level.WARNING, "Could not insert plugin logger - one was already found: {}", LogManager.getLogManager().getLogger(this.getName()));
            logger = LogManager.getLogManager().getLogger(meta.getLoggerPrefix() != null ? meta.getLoggerPrefix() : meta.getName());
        }

        return logger;
    }

    private PaperPluginLogger(final PluginMeta meta) {
        super(meta.getLoggerPrefix() != null ? meta.getLoggerPrefix() : meta.getName(), null);
    }

    @Override
    public void setParent(final Logger parent) {
        if (this.getParent() != null) {
            this.warning("Ignoring attempt to change parent of plugin logger");
        } else {
            this.log(Level.FINE, "Setting plugin logger parent to {0}", parent);
            super.setParent(parent);
        }
    }

}
