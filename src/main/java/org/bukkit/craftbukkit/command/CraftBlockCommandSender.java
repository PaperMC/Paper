package org.bukkit.craftbukkit.command;

import net.minecraft.server.TileEntityCommandListener;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final TileEntityCommandListener commandBlock;

    public CraftBlockCommandSender(TileEntityCommandListener commandBlockListenerAbstract) {
        super();
        this.commandBlock = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return commandBlock.getWorld().getWorld().getBlockAt(commandBlock.getChunkCoordinates().x, commandBlock.getChunkCoordinates().y, commandBlock.getChunkCoordinates().z);
    }

    public void sendMessage(String message) {
    }

    public void sendMessage(String[] messages) {
    }

    public String getName() {
        return commandBlock.getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }
}
