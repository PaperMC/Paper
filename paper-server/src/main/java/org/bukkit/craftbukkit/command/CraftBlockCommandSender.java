package org.bukkit.craftbukkit.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {

    // For performance reasons, use one PermissibleBase for all command blocks.
    private static final PermissibleBase SHARED_PERM = new PermissibleBase(new ServerOperator() {

        @Override
        public boolean isOp() {
            return true;
        }

        @Override
        public void setOp(boolean value) {
            throw new UnsupportedOperationException("Cannot change operator status of a block");
        }
    });
    private final CommandSourceStack block;
    private final BlockEntity blockEntity;

    public CraftBlockCommandSender(CommandSourceStack commandBlockListenerAbstract, BlockEntity blockEntity) {
        super(CraftBlockCommandSender.SHARED_PERM);
        this.block = commandBlockListenerAbstract;
        this.blockEntity = blockEntity;
    }

    @Override
    public Block getBlock() {
        return CraftBlock.at(this.blockEntity.getLevel(), this.blockEntity.getBlockPos());
    }

    @Override
    public void sendMessage(String message) {
        for (Component component : CraftChatMessage.fromString(message)) {
            this.block.source.sendSystemMessage(component);
        }
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return this.block.getTextName();
    }

    @Override
    public void sendMessage(net.kyori.adventure.identity.Identity identity, net.kyori.adventure.text.Component message, net.kyori.adventure.audience.MessageType type) {
        this.block.source.sendSystemMessage(io.papermc.paper.adventure.PaperAdventure.asVanilla(message));
    }

    @Override
    public net.kyori.adventure.text.Component name() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.block.getDisplayName());
    }

    @Override
    public boolean isOp() {
        return CraftBlockCommandSender.SHARED_PERM.isOp();
    }

    @Override
    public void setOp(boolean value) {
        CraftBlockCommandSender.SHARED_PERM.setOp(value);
    }

    public CommandSourceStack getWrapper() {
        return this.block;
    }
}
