package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityFlowerPot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;

public class CraftFlowerPot extends CraftBlockState implements FlowerPot {

    private final TileEntityFlowerPot pot;

    public CraftFlowerPot(Block block) {
        super(block);

        pot = (TileEntityFlowerPot) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftFlowerPot(Material material, TileEntityFlowerPot pot) {
        super(material);

        this.pot = pot;
    }

    @Override
    public MaterialData getContents() {
        return (pot.d() == null) ? null : CraftMagicNumbers.getMaterial(pot.d()).getNewData((byte) pot.e()); // PAIL: rename
    }

    @Override
    public void setContents(MaterialData item) {
        if (item == null) {
            pot.a(null, 0);
        } else {
            pot.a(CraftMagicNumbers.getItem(item.getItemType()), item.getData()); // PAIL: rename
        }
    }
}
