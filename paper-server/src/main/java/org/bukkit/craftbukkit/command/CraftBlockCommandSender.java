package org.bukkit.craftbukkit.command;

import net.minecraft.server.ICommandListener;
import net.minecraft.server.IChatBaseComponent;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final ICommandListener block;

    public CraftBlockCommandSender(ICommandListener commandBlockListenerAbstract) {
        super();
        this.block = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return block.getWorld().getWorld().getBlockAt(block.getChunkCoordinates().getX(), block.getChunkCoordinates().getY(), block.getChunkCoordinates().getZ());
    }

    public void sendMessage(String message) {
        for (IChatBaseComponent component : CraftChatMessage.fromString(message)) {
            block.sendMessage(component);
        }
    }

    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    public String getName() {
        return block.getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public ICommandListener getTileEntity() {
        return block;
    }
}
