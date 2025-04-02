package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
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

    public CraftBell(World world, BellBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBell(CraftBell state, Location location) {
        super(state, location);
    }

    @Override
    public boolean ring(Entity entity, BlockFace direction) {
        Preconditions.checkArgument(direction == null || direction.isCartesian(), "direction must be cartesian, given %s", direction);

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        if (blockEntity == null) {
            return false;
        }

        net.minecraft.world.entity.Entity nmsEntity = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        return ((BellBlock) Blocks.BELL).attemptToRing(nmsEntity, this.world.getHandle(), this.getPosition(), CraftBlock.blockFaceToNotch(direction));
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
