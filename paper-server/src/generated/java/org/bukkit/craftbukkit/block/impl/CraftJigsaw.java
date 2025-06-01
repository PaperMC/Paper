package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftJigsaw extends CraftBlockData implements Jigsaw {
    private static final EnumProperty<FrontAndTop> ORIENTATION = JigsawBlock.ORIENTATION;

    public CraftJigsaw(BlockState state) {
        super(state);
    }

    @Override
    public org.bukkit.block.Orientation getOrientation() {
        return this.get(ORIENTATION, org.bukkit.block.Orientation.class);
    }

    @Override
    public void setOrientation(final org.bukkit.block.Orientation orientation) {
        Preconditions.checkArgument(orientation != null, "orientation cannot be null!");
        this.set(ORIENTATION, orientation);
    }
}
