
package org.bukkit.command;

public interface ProxiedCommandSender extends CommandSender {

    /**
     * Returns the CommandSender which triggered this proxied command
     *
     * @return the caller which triggered the command
     */
    CommandSender getCaller();

    /**
     * Returns the CommandSender which is being used to call the command
     *
     * @return the caller which the command is being run as
     */
    CommandSender getCallee();

}
