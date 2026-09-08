package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceExtractEvent extends CraftBlockExpEvent implements FurnaceExtractEvent {

    private final Player player;
    private final ItemStack item;

    public CraftFurnaceExtractEvent(final Player player, final Block block, final ItemStack item, final int exp) {
        super(block, exp);
        this.player = player;
        this.item = item;
    }

    public CraftFurnaceExtractEvent(
        final ServerPlayer player,
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack item,
        final int exp
    ) {
        this(player.getBukkitEntity(), CraftBlock.at(level, pos), CraftItemStack.asCraftMirror(item), exp);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getItemStack() {
        return this.item.clone();
    }

    @Override
    public int getItemAmount() {
        return this.item.getAmount();
    }
}
