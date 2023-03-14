package org.bukkit.craftbukkit.block;

import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.entity.SuspiciousSandBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.SuspiciousSand;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

public class CraftSuspiciousSand extends CraftBlockEntityState<SuspiciousSandBlockEntity> implements SuspiciousSand {

    public CraftSuspiciousSand(World world, SuspiciousSandBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getSnapshot().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        getSnapshot().item = CraftItemStack.asNMSCopy(item);
    }

    @Override
    public void applyTo(SuspiciousSandBlockEntity lootable) {
        super.applyTo(lootable);

        if (this.getSnapshot().lootTable == null) {
            lootable.setLootTable((MinecraftKey) null, 0L);
        }
    }

    @Override
    public LootTable getLootTable() {
        if (getSnapshot().lootTable == null) {
            return null;
        }

        MinecraftKey key = getSnapshot().lootTable;
        return Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(key));
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
        MinecraftKey key = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
        getSnapshot().setLootTable(key, seed);
    }
}
