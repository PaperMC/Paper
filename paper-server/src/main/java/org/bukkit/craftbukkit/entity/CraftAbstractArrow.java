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

    // Paper - moved to AbstractProjectile

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
        if (this.getHandle().getWeaponItem() == null) return null; // Paper - fix NPE
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

    // Paper start
    @Override
    public CraftItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(this.getHandle().getPickupItem());
    }

    @Override
    public void setItemStack(final ItemStack stack) {
        Preconditions.checkArgument(stack != null, "ItemStack cannot be null");
        this.getHandle().setPickupItemStack(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public void setLifetimeTicks(int ticks) {
        this.getHandle().life = ticks;
    }

    @Override
    public int getLifetimeTicks() {
        return this.getHandle().life;
    }

    @Override
    public org.bukkit.Sound getHitSound() {
        return org.bukkit.craftbukkit.CraftSound.minecraftToBukkit(this.getHandle().soundEvent);
    }

    @Override
    public void setHitSound(org.bukkit.Sound sound) {
        this.getHandle().setSoundEvent(org.bukkit.craftbukkit.CraftSound.bukkitToMinecraft(sound));
    }
    // Paper end

    // Paper start - Fix PickupStatus getting reset - Copy of CraftProjectile#setShooter, calling setOwner(Entity,boolean)
    @Override
    public void setShooter(org.bukkit.projectiles.ProjectileSource shooter, boolean resetPickupStatus) {
        if (shooter instanceof CraftEntity craftEntity) {
            this.getHandle().setOwner(craftEntity.getHandle(), resetPickupStatus);
        } else {
            this.getHandle().setOwner(null, resetPickupStatus);
        }
        this.getHandle().projectileSource = shooter;
    }
    // Paper end - Fix PickupStatus getting reset
}
