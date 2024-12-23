package org.bukkit.command;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.4.5
 */
public interface BlockCommandSender extends CommandSender {

    /**
     * Returns the block this command sender belongs to
     *
     * @return Block for the command sender
     */
    @NotNull
    public Block getBlock();
}
