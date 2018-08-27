package org.bukkit.craftbukkit.block;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.TileEntityLootable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftLootable<T extends TileEntityLootable> extends CraftContainer<T> implements Nameable, Lootable {

    public CraftLootable(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftLootable(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public String getCustomName() {
        T lootable = this.getSnapshot();
        return lootable.hasCustomName() ? CraftChatMessage.fromComponent(lootable.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);

        if (!this.getSnapshot().hasCustomName()) {
            lootable.setCustomName(null);
        }
        if (this.getSnapshot().getLootTable() == null) {
            lootable.setLootTable((MinecraftKey) null, 0L);
        }
    }

    @Override
    public LootTable getLootTable() {
        if (getSnapshot().getLootTable() == null) {
            return null;
        }

        MinecraftKey key = getSnapshot().getLootTable();
        return Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(key));
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public long getSeed() {
        return getSnapshotNBT().getLong("LootTableSeed"); // returns OL if an error occurred
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    private void setLootTable(LootTable table, long seed) {
        MinecraftKey key = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
        getSnapshot().setLootTable(key, seed);
    }
}
