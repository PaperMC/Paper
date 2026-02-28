package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerLevel;
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

    protected CraftCreakingHeart(CraftCreakingHeart state, @Nullable Location location) {
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

    @Nullable
    @Override
    public Creaking spawnCreaking() {
        this.requirePlaced();
        if (this.getBlockEntity().getLevel() instanceof ServerLevel serverLevel) {
            net.minecraft.world.entity.monster.creaking.Creaking creaking = CreakingHeartBlockEntity.spawnProtector(serverLevel, this.getBlockEntity());
            if (creaking != null) {
                this.getBlockEntity().setCreakingInfo(creaking);
                creaking.makeSound(SoundEvents.CREAKING_SPAWN);
                serverLevel.playSound(null, this.getBlockEntity().getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return creaking != null ? ((Creaking) creaking.getBukkitEntity()) : null;
        }
        return null;
    }

    @Nullable
    @Override
    public Location spreadResin() {
        this.requirePlaced();
        if (this.getBlockEntity().getLevel() instanceof ServerLevel serverLevel) {
            return this.getBlockEntity().spreadResin(serverLevel).map(blockPos -> CraftLocation.toBukkit(blockPos, this.getWorld())).orElse(null);
        }
        return null;
    }
}
