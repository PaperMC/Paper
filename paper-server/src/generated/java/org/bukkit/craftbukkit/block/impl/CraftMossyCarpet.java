package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.MossyCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.MossyCarpet;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftMossyCarpet extends CraftBlockData implements MossyCarpet {
    private static final BooleanProperty BASE = MossyCarpetBlock.BASE;

    private static final Map<BlockFace, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = MossyCarpetBlock.PROPERTY_BY_DIRECTION.entrySet().stream()
            .collect(Collectors.toMap(entry -> CraftBlock.notchToBlockFace(entry.getKey()), entry -> entry.getValue()));

    public CraftMossyCarpet(BlockState state) {
        super(state);
    }

    @Override
    public boolean isBottom() {
        return this.get(BASE);
    }

    @Override
    public void setBottom(final boolean bottom) {
        this.set(BASE, bottom);
    }

    @Override
    public org.bukkit.block.data.type.Wall.Height getHeight(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        EnumProperty<WallSide> property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        return this.get(property, org.bukkit.block.data.type.Wall.Height.class);
    }

    @Override
    public void setHeight(final BlockFace blockFace,
            final org.bukkit.block.data.type.Wall.Height height) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(height != null, "height cannot be null!");
        EnumProperty<WallSide> property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        this.set(property, height);
    }
}
