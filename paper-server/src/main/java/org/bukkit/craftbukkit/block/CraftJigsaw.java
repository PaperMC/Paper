package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<JigsawBlockEntity> implements Jigsaw {

    public CraftJigsaw(World world, JigsawBlockEntity blockEntity, boolean useSnapshot) {
        super(world, blockEntity, useSnapshot);
    }

    protected CraftJigsaw(CraftJigsaw state, Location location) {
        super(state, location);
    }

    @Override
    public CraftJigsaw copy() {
        return new CraftJigsaw(this, null);
    }

    @Override
    public CraftJigsaw copy(Location location) {
        return new CraftJigsaw(this, location);
    }
}
