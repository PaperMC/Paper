package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityConduit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Conduit;

public class CraftConduit extends CraftBlockEntityState<TileEntityConduit> implements Conduit {

    public CraftConduit(Block block) {
        super(block, TileEntityConduit.class);
    }

    public CraftConduit(Material material, TileEntityConduit te) {
        super(material, te);
    }
}
