package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.entity.vehicle.EntityMinecartContainer;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftMinecartContainer extends CraftMinecart implements Lootable {

    public CraftMinecartContainer(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityMinecartContainer getHandle() {
        return (EntityMinecartContainer) entity;
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(getHandle().lootTable);
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return getHandle().lootTableSeed;
    }

    private void setLootTable(LootTable table, long seed) {
        getHandle().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }
}
