package org.bukkit.craftbukkit.command;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final CommandListenerWrapper block;
    private final TileEntity tile;

    public CraftBlockCommandSender(CommandListenerWrapper commandBlockListenerAbstract, TileEntity tile) {
        super();
        this.block = commandBlockListenerAbstract;
        this.tile = tile;
    }

    @Override
    public Block getBlock() {
        return CraftBlock.at(tile.getLevel(), tile.getBlockPos());
    }

    @Override
    public void sendMessage(String message) {
        for (IChatBaseComponent component : CraftChatMessage.fromString(message)) {
            block.source.sendSystemMessage(component);
        }
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return block.getTextName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public CommandListenerWrapper getWrapper() {
        return block;
    }
}
