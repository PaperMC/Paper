package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {

    // Paper start
    private final static int NO_AGE_TIME = (int) Short.MIN_VALUE;
    private final static int NO_PICKUP_TIME = (int) Short.MAX_VALUE;
    // Paper end

    public CraftItem(CraftServer server, ItemEntity entity) {
        super(server, entity);
    }

    @Override
    public ItemEntity getHandle() {
        return (ItemEntity) this.entity;
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(this.getHandle().getItem());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.getHandle().setItem(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return this.getHandle().pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        this.getHandle().pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public void setUnlimitedLifetime(boolean unlimited) {
        if (unlimited) {
            // See EntityItem#INFINITE_LIFETIME
            this.getHandle().age = Short.MIN_VALUE;
        } else {
            this.getHandle().age = this.getTicksLived();
        }
    }

    @Override
    public boolean isUnlimitedLifetime() {
        return this.getHandle().age == Short.MIN_VALUE;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityItem (don't set if lifetime is unlimited)
        if (!this.isUnlimitedLifetime()) {
            this.getHandle().age = value;
        }
    }

    // Paper start
    @Override
    public boolean canMobPickup() {
        return this.getHandle().canMobPickup;
    }

    @Override
    public void setCanMobPickup(boolean canMobPickup) {
        this.getHandle().canMobPickup = canMobPickup;
    }

     @Override
     public boolean canPlayerPickup() {
        return this.getHandle().pickupDelay != NO_PICKUP_TIME;
     }

     @Override
     public void setCanPlayerPickup(boolean canPlayerPickup) {
        this.getHandle().pickupDelay = canPlayerPickup ? 0 : NO_PICKUP_TIME;
     }

     @Override
     public boolean willAge() {
        return this.getHandle().age != NO_AGE_TIME;
     }

     @Override
     public void setWillAge(boolean willAge) {
        this.getHandle().age = willAge ? 0 : NO_AGE_TIME;
     }

     @org.jetbrains.annotations.NotNull
     @Override
     public net.kyori.adventure.util.TriState getFrictionState() {
        return this.getHandle().frictionState;
     }

     @Override
     public void setFrictionState(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState state) {
         java.util.Objects.requireNonNull(state, "state may not be null");
         this.getHandle().frictionState = state;
     }

    @Override
    public int getHealth() {
        return this.getHandle().health;
    }

    @Override
    public void setHealth(int health) {
        if (health <= 0) {
            this.getHandle().getItem().onDestroyed(this.getHandle());
            this.getHandle().discard(org.bukkit.event.entity.EntityRemoveEvent.Cause.PLUGIN);
        } else {
            this.getHandle().health = health;
        }
    }
    // Paper end

    @Override
    public void setOwner(UUID uuid) {
        this.getHandle().setTarget(uuid);
    }

    @Override
    public UUID getOwner() {
        return this.getHandle().target;
    }

    @Override
    public void setThrower(UUID uuid) {
        this.getHandle().thrower = uuid;
    }

    @Override
    public UUID getThrower() {
        return this.getHandle().thrower;
    }

    @Override
    public String toString() {
        return "CraftItem";
    }
}
