package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftLootable<T extends RandomizableContainerBlockEntity> extends CraftContainer<T> implements Nameable, Lootable {

    public CraftLootable(World world, T tileEntity) {
        super(world, tileEntity);
    }

    protected CraftLootable(CraftLootable<T> state, Location location) {
        super(state, location);
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);

        if (this.getSnapshot().lootTable == null) {
            lootable.setLootTable(null, 0L);
        }
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getSnapshot().lootTable);
    }

    @Override
    public void setLootTable(LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public long getSeed() {
        return this.getSnapshot().lootTableSeed;
    }

    @Override
    public void setSeed(long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }

    public void setLootTable(LootTable table, long seed) {
        this.getSnapshot().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }

    @Override
    public abstract CraftLootable<T> copy();

    @Override
    public abstract CraftLootable<T> copy(Location location);
}
