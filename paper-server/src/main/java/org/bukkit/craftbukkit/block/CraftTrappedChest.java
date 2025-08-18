package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TrappedChest;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Nullable;

public class CraftTrappedChest extends CraftChest<TrappedChestBlockEntity> implements TrappedChest {

    public CraftTrappedChest(World world, TrappedChestBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftTrappedChest(CraftTrappedChest state, Location location) {
        super(state, location);
    }

    @Override
    public CraftTrappedChest copy() {
        return new CraftTrappedChest(this, null);
    }

    @Override
    public CraftTrappedChest copy(Location location) {
        return new CraftTrappedChest(this, location);
    }

    @Override
    public int getPower() {
        return this.getSnapshot().getSignal();
    }

    @Override
    public boolean isForcedPower() {
        return this.getSnapshot().getForcedSignal() != null;
    }

    @Override
    public void setForcedPower(@Nullable @IntRange(from = 0, to = 15) final Integer power) {
        this.getSnapshot().setForcedSignal(power);
    }

    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        int from = this.getBlockEntity().getSignal();
        int to = this.getSnapshot().getSignal();

        boolean result = super.update(force, applyPhysics);

        if (result && from != to) {
            this.getBlockEntity().updateRedStone(getNMSWorld(), getPosition(), getHandle(), from, to);
        }

        return result;
    }
}
