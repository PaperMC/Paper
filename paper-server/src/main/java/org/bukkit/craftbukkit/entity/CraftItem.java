package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {

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
