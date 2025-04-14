package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable<ShulkerBoxBlockEntity> implements ShulkerBox {

    public CraftShulkerBox(World world, ShulkerBoxBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftShulkerBox(CraftShulkerBox state, Location location) {
        super(state, location);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getBlockEntity());
    }

    @Override
    public DyeColor getColor() {
        net.minecraft.world.item.DyeColor color = ((ShulkerBoxBlock) CraftBlockType.bukkitToMinecraft(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getId());
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getBlockEntity().opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            net.minecraft.world.level.Level world = this.getBlockEntity().getLevel();
            world.blockEvent(this.getPosition(), this.getBlockEntity().getBlockState().getBlock(), 1, 1);
            world.playSound(null, this.getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        this.getBlockEntity().opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getBlockEntity().opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            net.minecraft.world.level.Level world = this.getBlockEntity().getLevel();
            world.blockEvent(this.getPosition(), this.getBlockEntity().getBlockState().getBlock(), 1, 0);
            world.playSound(null, this.getPosition(), SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F); // Paper - More Lidded Block API (Wrong sound)
        }
        this.getBlockEntity().opened = false;
    }

    @Override
    public CraftShulkerBox copy() {
        return new CraftShulkerBox(this, null);
    }

    @Override
    public CraftShulkerBox copy(Location location) {
        return new CraftShulkerBox(this, location);
    }

    // Paper start - More Lidded Block API
    @Override
    public boolean isOpen() {
        return getBlockEntity().opened;
    }
    // Paper end - More Lidded Block API
}
