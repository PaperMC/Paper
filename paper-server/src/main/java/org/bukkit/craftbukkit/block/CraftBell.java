package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.block.BlockBell;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBell;
import org.bukkit.World;
import org.bukkit.block.Bell;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class CraftBell extends CraftBlockEntityState<TileEntityBell> implements Bell {

    public CraftBell(World world, TileEntityBell tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBell(CraftBell state) {
        super(state);
    }

    @Override
    public boolean ring(Entity entity, BlockFace direction) {
        Preconditions.checkArgument(direction == null || direction.isCartesian(), "direction must be cartesian, given %s", direction);

        TileEntity tileEntity = getTileEntityFromWorld();
        if (tileEntity == null) {
            return false;
        }

        net.minecraft.world.entity.Entity nmsEntity = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        EnumDirection enumDirection = CraftBlock.blockFaceToNotch(direction);

        return ((BlockBell) Blocks.BELL).attemptToRing(nmsEntity, world.getHandle(), getPosition(), enumDirection);
    }

    @Override
    public boolean ring(Entity entity) {
        return ring(entity, null);
    }

    @Override
    public boolean ring(BlockFace direction) {
        return ring(null, direction);
    }

    @Override
    public boolean ring() {
        return ring(null, null);
    }

    @Override
    public boolean isShaking() {
        return getSnapshot().shaking;
    }

    @Override
    public int getShakingTicks() {
        return getSnapshot().ticks;
    }

    @Override
    public boolean isResonating() {
        return getSnapshot().resonating;
    }

    @Override
    public int getResonatingTicks() {
        return isResonating() ? getSnapshot().ticks : 0;
    }

    @Override
    public CraftBell copy() {
        return new CraftBell(this);
    }
}
