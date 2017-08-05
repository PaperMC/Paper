package org.bukkit.craftbukkit.block;

import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityFlowerPot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.material.MaterialData;

public class CraftFlowerPot extends CraftBlockEntityState<TileEntityFlowerPot> implements FlowerPot {

    private MaterialData contents;

    public CraftFlowerPot(Block block) {
        super(block, TileEntityFlowerPot.class);
    }

    public CraftFlowerPot(Material material, TileEntityFlowerPot te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityFlowerPot pot) {
        super.load(pot);

        contents = (pot.getItem() == null) ? null : CraftItemStack.asBukkitCopy(pot.getContents()).getData();
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
    public void applyTo(TileEntityFlowerPot pot) {
        super.applyTo(pot);

        pot.setContents(contents == null ? ItemStack.a : CraftItemStack.asNMSCopy(contents.toItemStack(1)));
    }
}
