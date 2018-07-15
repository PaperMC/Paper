package org.bukkit.craftbukkit.command;

import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.CommandListenerWrapper;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Vec3D;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final CommandListenerWrapper block;

    public CraftBlockCommandSender(CommandListenerWrapper commandBlockListenerAbstract) {
        super();
        this.block = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        Vec3D pos = block.getPosition();
        return block.getWorld().getWorld().getBlockAt((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public void sendMessage(String message) {
        for (IChatBaseComponent component : CraftChatMessage.fromString(message)) {
            block.base.sendMessage(component);
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

    public CommandListenerWrapper getWrapper() {
        return block;
    }
}
