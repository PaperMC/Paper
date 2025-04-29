package org.bukkit.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a command block.
 */
public interface CommandBlock extends TileState, io.papermc.paper.command.CommandBlockHolder { // Paper

    /**
     * Gets the command that this CommandBlock will run when powered.
     * This will never return null. If the CommandBlock does not have a
     * command, an empty String will be returned instead.
     *
     * @return Command that this CommandBlock will run when powered.
     */
    @NotNull
    public String getCommand();

    /**
     * Sets the command that this CommandBlock will run when powered.
     * Setting the command to null is the same as setting it to an empty
     * String.
     *
     * @param command Command that this CommandBlock will run when powered.
     */
    public void setCommand(@Nullable String command);

    /**
     * Gets the name of this CommandBlock. The name is used with commands
     * that this CommandBlock executes. This name will never be null, and
     * by default is "@".
     *
     * @return Name of this CommandBlock.
     * @deprecated in favour of {@link #name()}
     */
    @Deprecated // Paper
    @NotNull
    public String getName();

    /**
     * Sets the name of this CommandBlock. The name is used with commands
     * that this CommandBlock executes. Setting the name to null is the
     * same as setting it to "@".
     *
     * @param name New name for this CommandBlock.
     * @deprecated in favour of {@link #name(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setName(@Nullable String name);

    /**
     * Gets the name of this CommandBlock. The name is used with commands
     * that this CommandBlock executes. This name will never be null, and
     * by default is a {@link net.kyori.adventure.text.TextComponent} containing {@code @}.
     *
     * @return Name of this CommandBlock.
     */
    public net.kyori.adventure.text.@NotNull Component name();

    /**
     * Sets the name of this CommandBlock. The name is used with commands
     * that this CommandBlock executes. Setting the name to null is the
     * same as setting it to a {@link net.kyori.adventure.text.TextComponent} containing {@code @}.
     *
     * @param name New name for this CommandBlock.
     */
    public void name(net.kyori.adventure.text.@Nullable Component name);
}
