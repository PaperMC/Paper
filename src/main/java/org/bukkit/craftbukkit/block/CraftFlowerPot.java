package org.bukkit.craftbukkit.block;

import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityFlowerPot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.material.MaterialData;

public class CraftFlowerPot extends CraftBlockState implements FlowerPot {

    private final TileEntityFlowerPot pot;
    private MaterialData contents;

    public CraftFlowerPot(Block block) {
        super(block);

        pot = (TileEntityFlowerPot) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
        contents = (pot.getItem() == null) ? null : CraftItemStack.asBukkitCopy(pot.getContents()).getData();
    }

    public CraftFlowerPot(Material material, TileEntityFlowerPot te) {
        super(material);

        pot = te;
        contents = (pot.getItem() == null) ? null : CraftItemStack.asBukkitCopy(pot.getContents()).getData();
    }

    @Override
    public TileEntity getTileEntity() {
        return pot;
    }

    @Override
    public MaterialData getContents() {
        return contents;
    }

    @Override
    public void setContents(MaterialData item) {
        contents = item;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            pot.setContents(contents == null ? ItemStack.a : CraftItemStack.asNMSCopy(contents.toItemStack(1)));
            pot.update();
        }

        return result;
    }
}
