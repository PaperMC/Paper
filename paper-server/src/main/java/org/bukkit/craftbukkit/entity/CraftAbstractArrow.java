package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class CraftAbstractArrow extends AbstractProjectile implements AbstractArrow {

    public CraftAbstractArrow(CraftServer server, net.minecraft.world.entity.projectile.AbstractArrow entity) {
        super(server, entity);
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
    }

    @Override
    public int getKnockbackStrength() {
        return 0;
    }

    @Override
    public double getDamage() {
        return this.getHandle().getBaseDamage();
    }

    @Override
    public void setDamage(double damage) {
        Preconditions.checkArgument(damage >= 0, "Damage value (%s) must be positive", damage);
        this.getHandle().setBaseDamage(damage);
    }

    @Override
    public int getPierceLevel() {
        return this.getHandle().getPierceLevel();
    }

    @Override
    public void setPierceLevel(int pierceLevel) {
        Preconditions.checkArgument(0 <= pierceLevel && pierceLevel <= Byte.MAX_VALUE, "Pierce level (%s) out of range, expected 0 < level < 127", pierceLevel);

        this.getHandle().setPierceLevel((byte) pierceLevel);
    }

    @Override
    public boolean isCritical() {
        return this.getHandle().isCritArrow();
    }

    @Override
    public void setCritical(boolean critical) {
        this.getHandle().setCritArrow(critical);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof Entity) {
            this.getHandle().setOwner(((CraftEntity) shooter).getHandle());
        } else {
            this.getHandle().setOwner(null);
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public boolean isInBlock() {
        return this.getHandle().isInGround();
    }

    @Override
    public Block getAttachedBlock() {
        if (!this.isInBlock()) {
            return null;
        }

        BlockPos pos = this.getHandle().blockPosition();
        return this.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[this.getHandle().pickup.ordinal()];
    }

    @Override
    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkArgument(status != null, "PickupStatus cannot be null");
        this.getHandle().pickup = net.minecraft.world.entity.projectile.AbstractArrow.Pickup.byOrdinal(status.ordinal());
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityArrow
        this.getHandle().life = value;
    }

    @Override
    public boolean isShotFromCrossbow() {
        net.minecraft.world.item.ItemStack firedFromWeapon = this.getHandle().getWeaponItem();

        return firedFromWeapon != null && firedFromWeapon.is(Items.CROSSBOW);
    }

    @Override
    public void setShotFromCrossbow(boolean shotFromCrossbow) {
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().pickupItemStack);
    }

    @Override
    public void setItem(ItemStack item) {
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        this.getHandle().pickupItemStack = CraftItemStack.asNMSCopy(item);
    }

    @Override
    public ItemStack getWeapon() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getWeaponItem());
    }

    @Override
    public void setWeapon(ItemStack item) {
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        this.getHandle().firedFromWeapon = CraftItemStack.asNMSCopy(item);
    }

    @Override
    public net.minecraft.world.entity.projectile.AbstractArrow getHandle() {
        return (net.minecraft.world.entity.projectile.AbstractArrow) this.entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }
}
