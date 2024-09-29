package org.bukkit.craftbukkit.projectiles;

import com.google.common.base.Preconditions;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.IPosition;
import net.minecraft.core.dispenser.SourceBlock;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.IProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.block.BlockDispenser;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import net.minecraft.world.phys.Vec3D;
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
    private final TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getLevel().getWorld().getBlockAt(dispenserBlock.getBlockPos().getX(), dispenserBlock.getBlockPos().getY(), dispenserBlock.getBlockPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Preconditions.checkArgument(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        SourceBlock sourceblock = new SourceBlock((WorldServer) dispenserBlock.getLevel(), dispenserBlock.getBlockPos(), dispenserBlock.getBlockState(), dispenserBlock);
        // Copied from DispenseBehaviorProjectile
        EnumDirection enumdirection = (EnumDirection) sourceblock.state().getValue(BlockDispenser.FACING);
        net.minecraft.world.level.World world = dispenserBlock.getLevel();
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
        ProjectileItem.a dispenseConfig = projectileItem.createDispenseConfig();

        IPosition iposition = dispenseConfig.positionFunction().getDispensePosition(sourceblock, enumdirection);
        IProjectile launch = projectileItem.asProjectile(world, iposition, itemstack, enumdirection);

        if (Fireball.class.isAssignableFrom(projectile)) {
            EntityFireball customFireball = null;
            if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityTypes.WITHER_SKULL.create(world);
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = EntityTypes.DRAGON_FIREBALL.create(world);
            } else if (BreezeWindCharge.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityTypes.BREEZE_WIND_CHARGE.create(world);
            } else if (LargeFireball.class.isAssignableFrom(projectile)) {
                launch = customFireball = EntityTypes.FIREBALL.create(world);
            }

            if (customFireball != null) {
                customFireball.setPos(iposition.x(), iposition.y(), iposition.z());

                // Values from ItemFireball
                RandomSource randomsource = world.getRandom();
                double d0 = randomsource.triangle((double) enumdirection.getStepX(), 0.11485000000000001D);
                double d1 = randomsource.triangle((double) enumdirection.getStepY(), 0.11485000000000001D);
                double d2 = randomsource.triangle((double) enumdirection.getStepZ(), 0.11485000000000001D);
                Vec3D vec3d = new Vec3D(d0, d1, d2);
                customFireball.assignDirectionalMovement(vec3d, 0.1D);
            }
        }

        if (launch instanceof EntityArrow arrow) {
            arrow.pickup = EntityArrow.PickupStatus.ALLOWED;
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
