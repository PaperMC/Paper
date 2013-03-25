package org.bukkit.craftbukkit.command;

import net.minecraft.server.TileEntityCommand;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final TileEntityCommand commandBlock;

    public CraftBlockCommandSender(TileEntityCommand commandBlock) {
        super();
        this.commandBlock = commandBlock;
    }

    public Block getBlock() {
        return commandBlock.getWorld().getWorld().getBlockAt(commandBlock.x, commandBlock.y, commandBlock.z);
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
