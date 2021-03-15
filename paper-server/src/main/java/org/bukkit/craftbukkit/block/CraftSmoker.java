package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntitySmoker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace<TileEntitySmoker> implements Smoker {

    public CraftSmoker(Block block) {
        super(block, TileEntitySmoker.class);
    }

    public CraftSmoker(Material material, TileEntitySmoker te) {
        super(material, te);
    }
}
