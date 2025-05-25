package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Random;
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
        return (FireworkMeta) CraftItemStack.getItemMeta(this.getHandle().getEntityData().get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM), org.bukkit.inventory.ItemType.FIREWORK_ROCKET); // Paper - Expose firework item directly
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        applyFireworkEffect(meta); // Paper - Expose firework item directly

        // Copied from FireworkRocketEntity constructor, update firework lifetime/power
        this.getHandle().lifetime = 10 * (1 + meta.getPower()) + this.random.nextInt(6) + this.random.nextInt(7);

        this.getHandle().getEntityData().markDirty(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
    }

    @Override
    public boolean setAttachedTo(LivingEntity entity) {
        if (this.isDetonated()) {
            return false;
        }

        this.getHandle().attachedToEntity = (entity != null) ? ((CraftLivingEntity) entity).getHandle() : null;
        // Paper start - update entity data
        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ATTACHED_TO_TARGET,
            entity != null ? java.util.OptionalInt.of(entity.getEntityId()) : java.util.OptionalInt.empty());
        // Paper end - update entity data
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
    public int getLife() {
        return this.getHandle().life;
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
    public int getMaxLife() {
        return this.getHandle().lifetime;
    }

    @Override
    public void detonate() {
        this.setLife(this.getMaxLife() + 1);
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
    public java.util.UUID getSpawningEntity() {
        return getHandle().spawningEntity;
    }

    // Paper start - Expose firework item directly + manually setting flight
    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack itemStack) {
        FireworkMeta meta = getFireworkMeta();
        ItemStack nmsItem = itemStack == null ? FireworkRocketEntity.getDefaultItem() : CraftItemStack.asNMSCopy(itemStack);
        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, nmsItem);

        applyFireworkEffect(meta);
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

    void applyFireworkEffect(FireworkMeta meta) {
        ItemStack item = this.getHandle().getItem();
        CraftItemStack.applyMetaToItem(item, meta);

        this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, item);
    }
    // Paper end - Expose firework item directly + manually setting flight
}
