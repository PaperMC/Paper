package com.destroystokyo.paper.loottable;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityMinecartContainer;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class PaperMinecartLootableInventory implements PaperLootableEntityInventory {

    private EntityMinecartContainer entity;

    public PaperMinecartLootableInventory(EntityMinecartContainer entity) {
        this.entity = entity;
    }

    @Override
    public org.bukkit.loot.LootTable getLootTable() {
        return entity.lootTable != null ? Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(entity.lootTable)) : null;
    }

    @Override
    public void setLootTable(org.bukkit.loot.LootTable table, long seed) {
        setLootTable(table);
        setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        entity.lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return entity.lootTableSeed;
    }

    @Override
    public void setLootTable(org.bukkit.loot.LootTable table) {
        entity.lootTable = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
    }

    @Override
    public PaperLootableInventoryData getLootableData() {
        return entity.lootableData;
    }

    @Override
    public Entity getHandle() {
        return entity;
    }

    @Override
    public LootableInventory getAPILootableInventory() {
        return (LootableInventory) entity.getBukkitEntity();
    }

    @Override
    public World getNMSWorld() {
        return entity.world;
    }
}
