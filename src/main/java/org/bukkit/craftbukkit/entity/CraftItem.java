package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityItem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

// Paper start
import javax.annotation.Nullable;
import java.util.UUID;
// Paper end

public class CraftItem extends CraftEntity implements Item {
    private final EntityItem item;

    public CraftItem(CraftServer server, Entity entity, EntityItem item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, EntityItem entity) {
        this(server, entity, entity);
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.getItemStack());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        item.setItemStack(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return item.pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        item.pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityItem
        item.age = value;
    }

    // Paper Start
    public boolean canMobPickup() {
        return item.canMobPickup;
    }

    public void setCanMobPickup(boolean canMobPickup) {
        item.canMobPickup = canMobPickup;
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return item.getOwner();
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        item.setOwner(owner);
    }

    @Nullable
    @Override
    public UUID getThrower() {
        return item.getThrower();
    }

    @Override
    public void setThrower(@Nullable UUID thrower) {
        item.setThrower(thrower);
    }
    // Paper End

    @Override
    public String toString() {
        return "CraftItem";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
