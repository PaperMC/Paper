package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityStructure;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftStructureBlock extends CraftBlockState {

    private final TileEntityStructure structure;

    public CraftStructureBlock(Block block) {
        super(block);

        this.structure = (TileEntityStructure) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftStructureBlock(Material material, TileEntityStructure structure) {
        super(material);

        this.structure = structure;
    }

    @Override
    public TileEntity getTileEntity() {
        return structure;
    }
}
