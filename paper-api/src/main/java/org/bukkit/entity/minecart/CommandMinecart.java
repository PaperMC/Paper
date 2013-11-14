package org.bukkit.entity.minecart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;

public interface CommandMinecart extends Minecart, CommandSender {

    /**
     * Gets the command that this CommandMinecart will run when activated.
     * This will never return null.  If the CommandMinecart does not have a
     * command, an empty String will be returned instead.
     *
     * @return Command that this CommandMinecart will run when powered.
     */
    public String getCommand();

    /**
     * Sets the command that this CommandMinecart will run when activated.
     * Setting the command to null is the same as setting it to an empty
     * String.
     *
     * @param command Command that this CommandMinecart will run when
     *     activated.
     */
    public void setCommand(String command);

    /**
     * Sets the name of this CommandMinecart.  The name is used with commands
     * that this CommandMinecart executes.  Setting the name to null is the
     * same as setting it to "@".
     *
     * @param name New name for this CommandMinecart.
     */
    public void setName(String name);

}
