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
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public AbstractChestBoat getHandle() {
        return (AbstractChestBoat) this.entity;
    }

    @Override
    public String toString() {
        return "CraftChestBoat";
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void setLootTable(LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().getContainerLootTable());
    }

    @Override
    public void setSeed(long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return this.getHandle().getContainerLootTableSeed();
    }

    private void setLootTable(LootTable table, long seed) {
        this.getHandle().setContainerLootTable(CraftLootTable.bukkitToMinecraft(table));
        this.getHandle().setContainerLootTableSeed(seed);
    }
}
