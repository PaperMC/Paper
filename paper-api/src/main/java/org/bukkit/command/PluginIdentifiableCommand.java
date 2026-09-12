package org.bukkit.command;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used by the help system to group commands into
 * sub-indexes based on the {@link Plugin} they are a part of. Custom command
 * implementations will need to implement this interface to have a sub-index
 * automatically generated on the plugin's behalf.
 *
 * @deprecated plugin developers should prefer to use the
 *     <a href="https://docs.papermc.io/paper/dev/command-api/basics/introduction/">Brigadier command API</a>
 */
@Deprecated(since = "26.3")
public interface PluginIdentifiableCommand {

    /**
     * Gets the owner of this PluginIdentifiableCommand.
     *
     * @return Plugin that owns this PluginIdentifiableCommand.
     */
    @NotNull
    public Plugin getPlugin();
}
