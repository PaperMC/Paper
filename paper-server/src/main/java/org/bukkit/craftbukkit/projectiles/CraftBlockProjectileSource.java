package org.bukkit.craftbukkit.projectiles;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractWindCharge;
import org.bukkit.entity.BreezeWindCharge;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final DispenserBlockEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserBlockEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return this.dispenserBlock.getLevel().getWorld().getBlockAt(this.dispenserBlock.getBlockPos().getX(), this.dispenserBlock.getBlockPos().getY(), this.dispenserBlock.getBlockPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Preconditions.checkArgument(this.getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        BlockSource sourceblock = new BlockSource((ServerLevel) this.dispenserBlock.getLevel(), this.dispenserBlock.getBlockPos(), this.dispenserBlock.getBlockState(), this.dispenserBlock);
        // Copied from DispenseBehaviorProjectile
        Direction enumdirection = (Direction) sourceblock.state().getValue(DispenserBlock.FACING);
        net.minecraft.world.level.Level world = this.dispenserBlock.getLevel();
        net.minecraft.world.item.Item item = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            item = Items.SNOWBALL;
        } else if (Egg.class.isAssignableFrom(projectile)) {
            item = Items.EGG;
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            item = Items.ENDER_PEARL;
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            item = Items.EXPERIENCE_BOTTLE;
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                item = Items.LINGERING_POTION;
            } else {
                item = Items.SPLASH_POTION;
            }
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                item = Items.TIPPED_ARROW;
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                item = Items.SPECTRAL_ARROW;
            } else {
                item = Items.ARROW;
            }
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            if (AbstractWindCharge.class.isAssignableFrom(projectile)) {
                item = Items.WIND_CHARGE;
            } else {
                item = Items.FIRE_CHARGE;
            }
        } else if (Firework.class.isAssignableFrom(projectile)) {
            item = Items.FIREWORK_ROCKET;
        }

        Preconditions.checkArgument(item instanceof ProjectileItem, "Projectile not supported");

        ItemStack itemstack = new ItemStack(item);
        ProjectileItem projectileItem = (ProjectileItem) item;
        ProjectileItem.DispenseConfig dispenseConfig = projectileItem.createDispenseConfig();

        Position iposition = dispenseConfig.positionFunction().getDispensePosition(sourceblock, enumdirection);
        net.minecraft.world.entity.projectile.Projectile launch = projectileItem.asProjectile(world, iposition, itemstack, enumdirection);

        if (Fireball.class.isAssignableFrom(projectile)) {
            AbstractHurtingProjectile customFireball = null;
            if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityType.WITHER_SKULL.create(world, EntitySpawnReason.TRIGGERED);
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = EntityType.DRAGON_FIREBALL.create(world, EntitySpawnReason.TRIGGERED);
            } else if (BreezeWindCharge.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityType.BREEZE_WIND_CHARGE.create(world, EntitySpawnReason.TRIGGERED);
            } else if (LargeFireball.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityType.FIREBALL.create(world, EntitySpawnReason.TRIGGERED);
            }

            if (customFireball != null) {
                customFireball.setPos(iposition.x(), iposition.y(), iposition.z());

                // Values from ItemFireball
                RandomSource randomsource = world.getRandom();
                double d0 = randomsource.triangle((double) enumdirection.getStepX(), 0.11485000000000001D);
                double d1 = randomsource.triangle((double) enumdirection.getStepY(), 0.11485000000000001D);
                double d2 = randomsource.triangle((double) enumdirection.getStepZ(), 0.11485000000000001D);
                Vec3 vec3d = new Vec3(d0, d1, d2);
                customFireball.assignDirectionalMovement(vec3d, 0.1D);
            }
        }

        if (launch instanceof net.minecraft.world.entity.projectile.AbstractArrow arrow) {
            arrow.pickup = net.minecraft.world.entity.projectile.AbstractArrow.Pickup.ALLOWED;
        }
        launch.projectileSource = this;
        projectileItem.shoot(launch, (double) enumdirection.getStepX(), (double) enumdirection.getStepY(), (double) enumdirection.getStepZ(), dispenseConfig.power(), dispenseConfig.uncertainty());

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
