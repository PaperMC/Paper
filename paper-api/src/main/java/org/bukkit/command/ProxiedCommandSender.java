
package org.bukkit.command;

import org.jetbrains.annotations.NotNull;

public interface ProxiedCommandSender extends CommandSender, net.kyori.adventure.audience.ForwardingAudience.Single { // Paper

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

    // Paper start
    @NotNull
    @Override
    default net.kyori.adventure.audience.Audience audience() {
        return this.getCaller();
    }
    // Paper end
}
