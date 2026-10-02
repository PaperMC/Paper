package org.bukkit.command;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// This interface is used by the help system to group commands into
/// sub-indexes based on the [Plugin] they are a part of. Custom command
/// implementations will need to implement this interface to have a sub-index
/// automatically generated on the plugin's behalf.
///
/// @apiNote plugin developers should prefer to use the
///     [Brigadier command API](https://docs.papermc.io/paper/dev/command-api/basics/introduction/).
///     For a direct alternative to Bukkit commands, [Basic commands](https://docs.papermc.io/paper/dev/command-api/misc/basic-command/) are recommended
@ApiStatus.Obsolete(since = "26.3")
public interface PluginIdentifiableCommand {

    /// Gets the owner of this PluginIdentifiableCommand.
    ///
    /// @return Plugin that owns this PluginIdentifiableCommand.
    @NotNull
    public Plugin getPlugin();
}
