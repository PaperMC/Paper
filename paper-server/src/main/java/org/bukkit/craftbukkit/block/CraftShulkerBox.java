package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.block.BlockShulkerBox;
import net.minecraft.world.level.block.entity.TileEntityShulkerBox;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable<TileEntityShulkerBox> implements ShulkerBox {

    public CraftShulkerBox(World world, TileEntityShulkerBox tileEntity) {
        super(world, tileEntity);
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

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public DyeColor getColor() {
        EnumColor color = ((BlockShulkerBox) CraftMagicNumbers.getBlock(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getColorIndex());
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            net.minecraft.world.level.World world = getTileEntity().getWorld();
            world.playBlockAction(getPosition(), getTileEntity().getBlock().getBlock(), 1, 1);
            world.playSound(null, getPosition(), SoundEffects.SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            net.minecraft.world.level.World world = getTileEntity().getWorld();
            world.playBlockAction(getPosition(), getTileEntity().getBlock().getBlock(), 1, 0);
            world.playSound(null, getPosition(), SoundEffects.SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }
}
