package org.bukkit.craftbukkit.projectiles;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;
import java.util.function.Consumer;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final DispenserBlockEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserBlockEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return CraftBlock.at(this.dispenserBlock.getLevel(), this.dispenserBlock.getBlockPos());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity, Consumer<? super T> function) {
        Preconditions.checkArgument(this.getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");

        // Copied from DispenserBlock#dispenseFrom
        BlockSource blockSource = new BlockSource((ServerLevel) this.dispenserBlock.getLevel(), this.dispenserBlock.getBlockPos(), this.dispenserBlock.getBlockState(), this.dispenserBlock);
        // Copied from ProjectileDispenseBehavior
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        net.minecraft.world.level.Level world = this.dispenserBlock.getLevel();
        net.minecraft.world.item.Item item = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            item = Items.SNOWBALL;
        } else if (Egg.class.isAssignableFrom(projectile)) {
            item = Items.EGG;
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
            } else if (org.bukkit.entity.Arrow.class.isAssignableFrom(projectile)) { // disallow trident
                item = Items.ARROW;
            }
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            if (org.bukkit.entity.WindCharge.class.isAssignableFrom(projectile)) {
                item = Items.WIND_CHARGE;
            } else if (org.bukkit.entity.SmallFireball.class.isAssignableFrom(projectile)) { // only allow firing fire charges
                item = Items.FIRE_CHARGE;
            }
        } else if (Firework.class.isAssignableFrom(projectile)) {
            item = Items.FIREWORK_ROCKET;
        }

        Preconditions.checkArgument(item instanceof ProjectileItem, "Projectile '%s' not supported", projectile.getSimpleName());

        ItemStack itemstack = new ItemStack(item);
        ProjectileItem projectileItem = (ProjectileItem) item;
        ProjectileItem.DispenseConfig dispenseConfig = projectileItem.createDispenseConfig();

        Position position = dispenseConfig.positionFunction().getDispensePosition(blockSource, direction);
        net.minecraft.world.entity.projectile.Projectile launch = projectileItem.asProjectile(world, position, itemstack, direction);
        launch.projectileSource = this;
        projectileItem.shoot(launch, direction.getStepX(), direction.getStepY(), direction.getStepZ(), dispenseConfig.power(), dispenseConfig.uncertainty());

        if (velocity != null) {
            launch.getBukkitEntity().setVelocity(velocity);
        }
        if (function != null) {
            function.accept((T) launch.getBukkitEntity());
        }

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
