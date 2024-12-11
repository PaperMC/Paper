package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Bell;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class CraftBell extends CraftBlockEntityState<BellBlockEntity> implements Bell {

    public CraftBell(World world, BellBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBell(CraftBell state, Location location) {
        super(state, location);
    }

    @Override
    public boolean ring(Entity entity, BlockFace direction) {
        Preconditions.checkArgument(direction == null || direction.isCartesian(), "direction must be cartesian, given %s", direction);

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (tileEntity == null) {
            return false;
        }

        net.minecraft.world.entity.Entity nmsEntity = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        Direction enumDirection = CraftBlock.blockFaceToNotch(direction);

        return ((BellBlock) Blocks.BELL).attemptToRing(nmsEntity, this.world.getHandle(), this.getPosition(), enumDirection);
    }

    @Override
    public boolean ring(Entity entity) {
        return this.ring(entity, null);
    }

    @Override
    public boolean ring(BlockFace direction) {
        return this.ring(null, direction);
    }

    @Override
    public boolean ring() {
        return this.ring(null, null);
    }

    @Override
    public boolean isShaking() {
        return this.getSnapshot().shaking;
    }

    @Override
    public int getShakingTicks() {
        return this.getSnapshot().ticks;
    }

    @Override
    public boolean isResonating() {
        return this.getSnapshot().resonating;
    }

    @Override
    public int getResonatingTicks() {
        return this.isResonating() ? this.getSnapshot().ticks : 0;
    }

    @Override
    public CraftBell copy() {
        return new CraftBell(this, null);
    }

    @Override
    public CraftBell copy(Location location) {
        return new CraftBell(this, location);
    }
}
