package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreakingHeart;
import org.bukkit.craftbukkit.entity.CraftCreaking;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Creaking;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftCreakingHeart extends CraftBlockEntityState<CreakingHeartBlockEntity> implements CreakingHeart {

    public CraftCreakingHeart(World world, CreakingHeartBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftCreakingHeart(CraftCreakingHeart state, Location location) {
        super(state, location);
    }

    @Override
    public CraftCreakingHeart copy() {
        return new CraftCreakingHeart(this, null);
    }

    @Override
    public CraftCreakingHeart copy(Location location) {
        return new CraftCreakingHeart(this, location);
    }

    @Override
    public @Nullable Creaking getCreaking() {
        if (!this.isPlaced()) {
            return null;
        }
        return this.getBlockEntity().getCreakingProtector().map(creaking -> ((Creaking) creaking.getBukkitEntity())).orElse(null);
    }

    @Override
    public void setCreaking(@Nullable final Creaking creaking) {
        this.requirePlaced();
        if (creaking == null) {
            this.getBlockEntity().clearCreakingInfo();
        } else {
            Preconditions.checkArgument(this.getLocation().getWorld().equals(creaking.getLocation().getWorld()), "the location of this creaking must be in the same world than this CreakingHeart");
            this.getBlockEntity().setCreakingInfo(((CraftCreaking) creaking).getHandle());
        }
    }

    @Override
    public int getCreakingRemovalDistance() {
        return this.getSnapshot().distanceCreakingTooFar;
    }

    @Override
    public void setCreakingRemovalDistance(final int distance) {
        Preconditions.checkArgument(distance >= 0, "the distance must be >= 0");
        this.getSnapshot().distanceCreakingTooFar = distance;
    }

    @Nullable
    @Override
    public Creaking spawnCreaking() {
        this.requirePlaced();
        if (!this.getBlockEntity().hasLevel()) {
            return null;
        }
        net.minecraft.world.entity.monster.creaking.Creaking creaking = CreakingHeartBlockEntity.spawnProtector(this.getBlockEntity().getLevel().getMinecraftWorld(), this.getBlockEntity());
        if (creaking != null) {
            this.getBlockEntity().setCreakingInfo(creaking);
            creaking.makeSound(SoundEvents.CREAKING_SPAWN);
            this.getBlockEntity().getLevel().playSound(null, this.getBlockEntity().getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return creaking != null ? ((Creaking) creaking.getBukkitEntity()) : null;
    }

    @Nullable
    @Override
    public Location spreadResin() {
        this.requirePlaced();
        return this.getBlockEntity().spreadResin().map(blockPos -> CraftLocation.toBukkit(blockPos, this.getWorld())).orElse(null);
    }
}
