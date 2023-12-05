package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {

    public CraftItem(CraftServer server, EntityItem entity) {
        super(server, entity);
    }

    @Override
    public EntityItem getHandle() {
        return (EntityItem) entity;
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(getHandle().getItem());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        getHandle().setItem(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return getHandle().pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        getHandle().pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public void setUnlimitedLifetime(boolean unlimited) {
        if (unlimited) {
            // See EntityItem#INFINITE_LIFETIME
            getHandle().age = Short.MIN_VALUE;
        } else {
            getHandle().age = getTicksLived();
        }
    }

    @Override
    public boolean isUnlimitedLifetime() {
        return getHandle().age == Short.MIN_VALUE;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityItem (don't set if lifetime is unlimited)
        if (!isUnlimitedLifetime()) {
            getHandle().age = value;
        }
    }

    @Override
    public void setOwner(UUID uuid) {
        getHandle().setTarget(uuid);
    }

    @Override
    public UUID getOwner() {
        return getHandle().target;
    }

    @Override
    public void setThrower(UUID uuid) {
        getHandle().thrower = uuid;
    }

    @Override
    public UUID getThrower() {
        return getHandle().thrower;
    }

    @Override
    public String toString() {
        return "CraftItem";
    }
}
