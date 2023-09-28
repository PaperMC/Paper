package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityJigsaw;
import org.bukkit.World;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<TileEntityJigsaw> implements Jigsaw {

    public CraftJigsaw(World world, TileEntityJigsaw tileEntity) {
        super(world, tileEntity);
    }

    protected CraftJigsaw(CraftJigsaw state) {
        super(state);
    }

    @Override
    public CraftJigsaw copy() {
        return new CraftJigsaw(this);
    }
}
