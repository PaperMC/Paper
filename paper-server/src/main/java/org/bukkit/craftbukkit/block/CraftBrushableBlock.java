package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BrushableBlock;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

public class CraftBrushableBlock extends CraftBlockEntityState<BrushableBlockEntity> implements BrushableBlock {

    public CraftBrushableBlock(World world, BrushableBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBrushableBlock(CraftBrushableBlock state, Location location) {
        super(state, location);
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getSnapshot().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        this.getSnapshot().item = CraftItemStack.asNMSCopy(item);
    }

    @Override
    public void applyTo(BrushableBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        if (this.getSnapshot().lootTable == null) {
            blockEntity.setLootTable(null, 0L);
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

    @Override // Paper - this is now an override
    public void setLootTable(LootTable table, long seed) { // Paper - make public since it overrides a public method
        this.getSnapshot().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }

    @Override
    public CraftBrushableBlock copy() {
        return new CraftBrushableBlock(this, null);
    }

    @Override
    public CraftBrushableBlock copy(Location location) {
        return new CraftBrushableBlock(this, location);
    }
}
