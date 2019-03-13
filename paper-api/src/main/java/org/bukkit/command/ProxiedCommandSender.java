
package org.bukkit.command;

import org.jetbrains.annotations.NotNull;

public interface ProxiedCommandSender extends CommandSender {

    /**
     * Returns the CommandSender which triggered this proxied command
     *
     * @return the caller which triggered the command
     */
    @NotNull
    CommandSender getCaller();

    /**
     * Returns the CommandSender which is being used to call the command
     *
     * @return the caller which the command is being run as
     */
    @NotNull
    CommandSender getCallee();

}
