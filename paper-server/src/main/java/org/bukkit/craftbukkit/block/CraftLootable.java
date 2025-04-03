package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftLootable<T extends RandomizableContainerBlockEntity> extends CraftContainer<T> implements Nameable, Lootable, com.destroystokyo.paper.loottable.PaperLootableBlockInventory { // Paper

    public CraftLootable(World world, T blockEntity) {
        super(world, blockEntity);
    }

    protected CraftLootable(CraftLootable<T> state, Location location) {
        super(state, location);
    }

    @Override
    public void applyTo(T blockEntity) {
        super.applyTo(blockEntity);

        if (this.getSnapshot().lootTable == null) {
            blockEntity.setLootTable(null, 0L);
        }
    }

    // Paper start - move to PaperLootableBlockInventory
    @Override
    public net.minecraft.world.level.Level getNMSWorld() {
        return ((org.bukkit.craftbukkit.CraftWorld) this.getWorld()).getHandle();
    }

    @Override
    public net.minecraft.world.RandomizableContainer getRandomizableContainer() {
        return this.getSnapshot();
    }
    // Paper end - move to PaperLootableBlockInventory

    @Override
    public abstract CraftLootable<T> copy();

    @Override
    public abstract CraftLootable<T> copy(Location location);
}
