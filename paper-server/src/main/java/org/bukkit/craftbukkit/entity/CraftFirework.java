package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;

public class CraftFirework extends CraftProjectile implements Firework {

    private final Random random = new Random();

    public CraftFirework(CraftServer server, FireworkRocketEntity entity) {
        super(server, entity);
    }

    @Override
    public FireworkRocketEntity getHandle() {
        return (FireworkRocketEntity) this.entity;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) CraftItemStack.getItemMeta(this.getHandle().getItem(), org.bukkit.inventory.ItemType.FIREWORK_ROCKET);
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        final ItemStack item = this.getHandle().getItem();
        CraftItemStack.applyMetaToItem(item, meta);

        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, item);

        // Copied from FireworkRocketEntity constructor, update firework lifetime/power
        this.getHandle().lifetime = 10 * (1 + meta.getPower()) + this.random.nextInt(6) + this.random.nextInt(7);
    }

    @Override
    public boolean setAttachedTo(LivingEntity entity) {
        if (this.isDetonated()) {
            return false;
        }

        this.getHandle().attachedToEntity = (entity != null) ? ((CraftLivingEntity) entity).getHandle() : null;
        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET, entity != null ? OptionalInt.of(entity.getEntityId()) : OptionalInt.empty());
        return true;
    }

    @Override
    public LivingEntity getAttachedTo() {
        net.minecraft.world.entity.LivingEntity entity = this.getHandle().attachedToEntity;
        return (entity != null) ? (LivingEntity) entity.getBukkitEntity() : null;
    }

    @Override
    public boolean setLife(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be greater than or equal to 0");

        if (this.isDetonated()) {
            return false;
        }

        this.getHandle().life = ticks;
        return true;
    }

    @Override
    public boolean setMaxLife(int ticks) {
        Preconditions.checkArgument(ticks > 0, "ticks must be greater than 0");

        if (this.isDetonated()) {
            return false;
        }

        this.getHandle().lifetime = ticks;
        return true;
    }

    @Override
    public void detonate() {
        this.setLife(this.getTicksToDetonate() + 1);
    }

    @Override
    public boolean isDetonated() {
        return this.getHandle().life > this.getHandle().lifetime;
    }

    @Override
    public boolean isShotAtAngle() {
        return this.getHandle().isShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_SHOT_AT_ANGLE, shotAtAngle);
    }

    @Override
    public UUID getSpawningEntity() {
        return this.getHandle().spawningEntity;
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack itemStack) {
        ItemStack nmsItem = itemStack == null ? FireworkRocketEntity.getDefaultItem() : CraftItemStack.asNMSCopy(itemStack);
        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, nmsItem);
    }

    @Override
    public int getTicksFlown() {
        return this.getHandle().life;
    }

    @Override
    public void setTicksFlown(int ticks) {
        this.getHandle().life = ticks;
    }

    @Override
    public int getTicksToDetonate() {
        return this.getHandle().lifetime;
    }

    @Override
    public void setTicksToDetonate(int ticks) {
        this.getHandle().lifetime = ticks;
    }
}
