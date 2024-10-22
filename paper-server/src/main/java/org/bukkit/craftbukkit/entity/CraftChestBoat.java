package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

public abstract class CraftChestBoat extends CraftBoat implements org.bukkit.entity.ChestBoat {

    private final Inventory inventory;

    public CraftChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    @Override
    public AbstractChestBoat getHandle() {
        return (AbstractChestBoat) entity;
    }

    @Override
    public String toString() {
        return "CraftChestBoat";
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(getHandle().getContainerLootTable());
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return getHandle().getContainerLootTableSeed();
    }

    private void setLootTable(LootTable table, long seed) {
        getHandle().setContainerLootTable(CraftLootTable.bukkitToMinecraft(table));
        getHandle().setContainerLootTableSeed(seed);
    }
}
