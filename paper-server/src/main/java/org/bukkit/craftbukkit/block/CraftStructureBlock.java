package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityStructure;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftStructureBlock extends CraftBlockEntityState<TileEntityStructure> {

    public CraftStructureBlock(Block block) {
        super(block, TileEntityStructure.class);
    }

    public CraftStructureBlock(Material material, TileEntityStructure structure) {
        super(material, structure);
    }
}
