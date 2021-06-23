package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Random;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;

public class CraftFirework extends CraftProjectile implements Firework {

    private final Random random = new Random();
    //private CraftItemStack item; // Paper - Remove usage, not accurate representation of current item.

    public CraftFirework(CraftServer server, FireworkRocketEntity entity) {
        super(server, entity);

        // Paper start - Expose firework item directly
//        ItemStack item = this.getHandle().getEntityData().get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
//
//        if (item.isEmpty()) {
//            item = new ItemStack(Items.FIREWORK_ROCKET);
//            this.getHandle().getEntityData().set(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM, item);
//        }
//
//        this.item = CraftItemStack.asCraftMirror(item);
//
//        // Ensure the item is a firework...
//        if (this.item.getType() != Material.FIREWORK_ROCKET) {
//            this.item.setType(Material.FIREWORK_ROCKET);
//        }
        // Paper end - Expose firework item directly
    }

    @Override
    public FireworkRocketEntity getHandle() {
        return (FireworkRocketEntity) this.entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) CraftItemStack.getItemMeta(this.getHandle().getEntityData().get(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM), org.bukkit.inventory.ItemType.FIREWORK_ROCKET); // Paper - Expose firework item directly
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        applyFireworkEffect(meta); // Paper - Expose firework item directly

        // Copied from EntityFireworks constructor, update firework lifetime/power
        this.getHandle().lifetime = 10 * (1 + meta.getPower()) + this.random.nextInt(6) + this.random.nextInt(7);

        this.getHandle().getEntityData().markDirty(FireworkRocketEntity.DATA_ID_FIREWORKS_ITEM);
    }

    @Override
    public boolean setAttachedTo(LivingEntity entity) {
        if (this.isDetonated()) {
            return false;
        }

        this.getHandle().attachedToEntity = (entity != null) ? ((CraftLivingEntity) entity).getHandle() : null;
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

    // Paper start
    @Override
    public java.util.UUID getSpawningEntity() {
        return getHandle().spawningEntity;
    }
    // Paper end
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
