package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreakingHeart;
import org.bukkit.entity.Creaking;
import org.jspecify.annotations.Nullable;

public class CraftCreakingHeart extends CraftBlockEntityState<CreakingHeartBlockEntity> implements CreakingHeart {

    public CraftCreakingHeart(World world, CreakingHeartBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftCreakingHeart(CraftCreakingHeart state, Location location) {
        super(state, location);
    }

    @Nullable
    @Override
    public Creaking getCreaking() {
        net.minecraft.world.entity.monster.creaking.Creaking creaking = this.getSnapshot().getCreaking();
        return creaking != null ? (Creaking) creaking.getBukkitEntity() : null;
    }

    @Override
    public boolean isActive() {
        return this.getSnapshot().getBlockState().getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE;
    }

    @Override
    public CraftCreakingHeart copy() {
        return new CraftCreakingHeart(this, null);
    }

    @Override
    public CraftCreakingHeart copy(Location location) {
        return new CraftCreakingHeart(this, location);
    }
}
