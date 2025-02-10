package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreakingHeart;
import org.bukkit.craftbukkit.entity.CraftCreaking;
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
        return this.getTileEntity().getCreakingProtector().map(creaking -> ((Creaking) creaking.getBukkitEntity())).orElse(null);
    }

    @Override
    public void setCreaking(@Nullable final Creaking creaking) {
        if (creaking == null) {
            this.getTileEntity().clearCreakingInfo();
        } else {
            Preconditions.checkArgument(this.getLocation().getWorld().equals(creaking.getLocation().getWorld()), "the location of this creaking must be in the same world than this CrakingHeart");
            this.getTileEntity().setCreakingInfo(((CraftCreaking) creaking).getHandle());
        }
    }

    @Nullable
    @Override
    public Creaking spawnCreaking() {
        net.minecraft.world.entity.monster.creaking.Creaking creaking = CreakingHeartBlockEntity.spawnProtector(this.getTileEntity().getLevel().getMinecraftWorld(), this.getTileEntity());
        return creaking != null ? ((Creaking) creaking.getBukkitEntity()) : null;
    }
}
