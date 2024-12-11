package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftMinecartContainer extends CraftMinecart implements Lootable {

    public CraftMinecartContainer(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public AbstractMinecartContainer getHandle() {
        return (AbstractMinecartContainer) this.entity;
    }

    @Override
    public void setLootTable(LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().lootTable);
    }

    @Override
    public void setSeed(long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return this.getHandle().lootTableSeed;
    }

    public void setLootTable(LootTable table, long seed) {
        this.getHandle().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }
}
