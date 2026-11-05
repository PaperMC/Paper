package io.papermc.paper.command;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

/**
 * Represents something that holds and runs a command like a command block.
 *
 * @see org.bukkit.block.CommandBlock
 * @see org.bukkit.entity.minecart.CommandMinecart
 */
public interface CommandBlockHolder {

    /**
     * Gets the command that this CommandBlock will run when powered.
     * This will never return null.  If the CommandBlock does not have a
     * command, an empty String will be returned instead.
     *
     * @return Command that this CommandBlock will run when activated.
     */
    String getCommand();

    /**
     * Sets the command that this CommandBlock will run when powered.
     * Setting the command to null is the same as setting it to an empty
     * String.
     *
     * @param command Command that this CommandBlock will run when activated.
     */
    void setCommand(@Nullable String command);

    /**
     * Gets the last output from this command block.
     *
     * @return the last output
     */
    Component lastOutput();

    /**
     * Sets the last output from this command block.
     *
     * @param lastOutput the last output
     */
    void lastOutput(@Nullable Component lastOutput);

    /**
     * Gets the success count from this command block.
     *
     * @return the success count
     * @see <a href="https://minecraft.wiki/wiki/Command_Block#Success_count">Command_Block#Success_count</a>
     */
    int getSuccessCount();

    /**
     * Sets the success count from this command block.
     *
     * @param successCount the success count
     * @see <a href="https://minecraft.wiki/wiki/Command_Block#Success_count">Command_Block#Success_count</a>
     */
    void setSuccessCount(int successCount);
}
