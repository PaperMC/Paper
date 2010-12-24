
package org.bukkit.plugin;

/**
 * Represents a plugin
 */
public abstract class Plugin {
    private boolean isEnabled = false;

    /**
     * Returns a value indicating whether or not this plugin is currently enabled
     * 
     * @return true if this plugin is enabled, otherwise false
     */
    public final boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Called when this plugin is enabled
     */
    protected abstract void onEnable();

    /**
     * Called when this plugin is disabled
     */
    protected abstract void onDisable();

    /**
     * Called when this plugin is first initialized
     */
    protected abstract void onInitialize();
}
