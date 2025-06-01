package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBed extends CraftBlockData implements Bed {
    private static final EnumProperty<Direction> FACING = BedBlock.FACING;

    private static final BooleanProperty OCCUPIED = BedBlock.OCCUPIED;

    private static final EnumProperty<BedPart> PART = BedBlock.PART;

    public CraftBed(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }

    @Override
    public boolean isOccupied() {
        return this.get(OCCUPIED);
    }

    @Override
    public void setOccupied(final boolean occupied) {
        this.set(OCCUPIED, occupied);
    }

    @Override
    public Bed.Part getPart() {
        return this.get(PART, Bed.Part.class);
    }

    @Override
    public void setPart(final Bed.Part part) {
        Preconditions.checkArgument(part != null, "part cannot be null!");
        this.set(PART, part);
    }
}
