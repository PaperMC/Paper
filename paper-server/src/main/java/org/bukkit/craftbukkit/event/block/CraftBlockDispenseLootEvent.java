package org.bukkit.craftbukkit.event.block;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

public class CraftBlockDispenseLootEvent extends CraftBlockEvent implements BlockDispenseLootEvent {

    private final @Nullable Player player;
    private List<ItemStack> dispensedLoot;
    private final LootTable lootTable;

    private boolean cancelled;

    public CraftBlockDispenseLootEvent(final @Nullable Player player, final Block block, final List<ItemStack> dispensedLoot, final LootTable lootTable) {
        super(block);
        this.player = player;
        this.dispensedLoot = dispensedLoot;
        this.lootTable = lootTable;
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
        this.dispensedLoot = dispensedLoot == null ? new ArrayList<>() : dispensedLoot;
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
