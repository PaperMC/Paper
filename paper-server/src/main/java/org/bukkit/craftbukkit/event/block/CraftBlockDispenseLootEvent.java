package org.bukkit.craftbukkit.event.block;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

public class CraftBlockDispenseLootEvent extends CraftBlockEvent implements BlockDispenseLootEvent {

    private final @Nullable Player player;
    private final List<ItemStack> dispensedLoot;
    private final LootTable lootTable;

    private boolean cancelled;

    public CraftBlockDispenseLootEvent(final @Nullable Player player, final Block block, final List<ItemStack> dispensedLoot, final LootTable lootTable) {
        super(block);
        this.player = player;
        this.dispensedLoot = dispensedLoot;
        this.lootTable = lootTable;
    }

    public CraftBlockDispenseLootEvent(
        final net.minecraft.world.entity.player.@Nullable Player player,
        final Level level,
        final BlockPos pos,
        final List<net.minecraft.world.item.ItemStack> dispensedLoot,
        final net.minecraft.world.level.storage.loot.LootTable lootTable
    ) {
        this(
            (Player) Optionull.map(player, Entity::getBukkitEntity),
            CraftBlock.at(level, pos),
            MCUtil.mutableTransform(dispensedLoot, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy),
            lootTable.craftLootTable
        );
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public List<ItemStack> getDispensedLoot() {
        return this.dispensedLoot;
    }

    @Override
    public void setDispensedLoot(final @Nullable List<ItemStack> dispensedLoot) {
        this.dispensedLoot.clear();
        if (dispensedLoot != null) {
            this.dispensedLoot.addAll(dispensedLoot);
        }
    }

    @Override
    public LootTable getLootTable() {
        return this.lootTable;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockDispenseLootEvent.getHandlerList();
    }
}
