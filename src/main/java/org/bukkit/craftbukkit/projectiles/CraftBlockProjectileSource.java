package org.bukkit.craftbukkit.projectiles;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

import net.minecraft.server.BlockDispenser;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityEnderPearl;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLargeFireball;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityThrownExpBottle;
import net.minecraft.server.EntityWitherSkull;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IPosition;
import net.minecraft.server.IProjectile;
import net.minecraft.server.MathHelper;
import net.minecraft.server.SourceBlock;
import net.minecraft.server.TileEntityDispenser;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getWorld().getWorld().getBlockAt(dispenserBlock.getPosition().getX(), dispenserBlock.getPosition().getY(), dispenserBlock.getPosition().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        SourceBlock isourceblock = new SourceBlock(dispenserBlock.getWorld(), dispenserBlock.getPosition());
        // Copied from DispenseBehaviorProjectile
        IPosition iposition = BlockDispenser.a(isourceblock);
        EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
        net.minecraft.server.World world = dispenserBlock.getWorld();
        net.minecraft.server.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, null);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityThrownExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            ((EntityArrow) launch).fromPlayer = 1;
            ((EntityArrow) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getAdjacentX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getAdjacentY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getAdjacentZ() * 0.3F);
            Random random = world.random;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).dirX = d3 / d6 * 0.1D;
                ((EntityFireball) launch).dirY = d4 / d6 * 0.1D;
                ((EntityFireball) launch).dirZ = d5 / d6 * 0.1D;
            } else {
                launch = new EntityLargeFireball(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).dirX = d3 / d6 * 0.1D;
                ((EntityFireball) launch).dirY = d4 / d6 * 0.1D;
                ((EntityFireball) launch).dirZ = d5 / d6 * 0.1D;
            }
            
            ((EntityFireball) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof IProjectile) {
            if (launch instanceof EntityProjectile) {
                ((EntityProjectile) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((IProjectile) launch).shoot((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
