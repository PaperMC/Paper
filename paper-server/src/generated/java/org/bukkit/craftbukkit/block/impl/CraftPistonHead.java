package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftPistonHead extends CraftBlockData implements PistonHead {
    private static final EnumProperty<Direction> FACING = PistonHeadBlock.FACING;

    private static final BooleanProperty SHORT = PistonHeadBlock.SHORT;

    private static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;

    public CraftPistonHead(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian(), "Invalid face, only cartesian face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }

    @Override
    public boolean isShort() {
        return this.get(SHORT);
    }

    @Override
    public void setShort(final boolean _short) {
        this.set(SHORT, _short);
    }

    @Override
    public org.bukkit.block.data.type.TechnicalPiston.Type getType() {
        return this.get(TYPE, org.bukkit.block.data.type.TechnicalPiston.Type.class);
    }

    @Override
    public void setType(final org.bukkit.block.data.type.TechnicalPiston.Type type) {
        Preconditions.checkArgument(type != null, "type cannot be null!");
        this.set(TYPE, type);
    }
}
