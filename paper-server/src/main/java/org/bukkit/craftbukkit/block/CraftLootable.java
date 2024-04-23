package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityLootable;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftLootable<T extends TileEntityLootable> extends CraftContainer<T> implements Nameable, Lootable {

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
        return CraftLootTable.minecraftToBukkit(getSnapshot().lootTable);
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public long getSeed() {
        return getSnapshot().lootTableSeed;
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    private void setLootTable(LootTable table, long seed) {
        getSnapshot().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }

    @Override
    public abstract CraftLootable<T> copy();

    @Override
    public abstract CraftLootable<T> copy(Location location);
}
