package com.destroystokyo.paper.loottable;

import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.TileEntityLootable;
import net.minecraft.server.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class PaperTileEntityLootableInventory implements PaperLootableBlockInventory {
    private TileEntityLootable tileEntityLootable;

    public PaperTileEntityLootableInventory(TileEntityLootable tileEntityLootable) {
        this.tileEntityLootable = tileEntityLootable;
    }

    @Override
    public org.bukkit.loot.LootTable getLootTable() {
        return tileEntityLootable.lootTable != null ? Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(tileEntityLootable.lootTable)) : null;
    }

    @Override
    public void setLootTable(org.bukkit.loot.LootTable table, long seed) {
        setLootTable(table);
        setSeed(seed);
    }

    @Override
    public void setLootTable(org.bukkit.loot.LootTable table) {
        tileEntityLootable.lootTable = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
    }

    @Override
    public void setSeed(long seed) {
        tileEntityLootable.lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return tileEntityLootable.lootTableSeed;
    }

    @Override
    public PaperLootableInventoryData getLootableData() {
        return tileEntityLootable.lootableData;
    }

    @Override
    public TileEntityLootable getTileEntity() {
        return tileEntityLootable;
    }

    @Override
    public LootableInventory getAPILootableInventory() {
        World world = tileEntityLootable.getWorld();
        if (world == null) {
            return null;
        }
        return (LootableInventory) getBukkitWorld().getBlockAt(MCUtil.toLocation(world, tileEntityLootable.getPosition())).getState();
    }

    @Override
    public World getNMSWorld() {
        return tileEntityLootable.getWorld();
    }
}
