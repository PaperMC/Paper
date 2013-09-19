package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityItemFrame;

import org.apache.commons.lang.Validate;

import org.bukkit.Rotation;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, EntityItemFrame entity) {
        super(server, entity);
    }

    public void setItem(org.bukkit.inventory.ItemStack item) {
        if (item == null || item.getTypeId() == 0) {
            getHandle().getDataWatcher().a(2, 5);
            getHandle().getDataWatcher().h(2);
        } else {
            getHandle().setItem(CraftItemStack.asNMSCopy(item));
        }
    }

    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItem());
    }

    public Rotation getRotation() {
        return toBukkitRotation(getHandle().getRotation());
    }

    Rotation toBukkitRotation(int value) {
        // Translate NMS rotation integer to Bukkit API
        switch (value) {
        case 0:
            return Rotation.NONE;
        case 1:
            return Rotation.CLOCKWISE;
        case 2:
            return Rotation.FLIPPED;
        case 3:
            return Rotation.COUNTER_CLOCKWISE;
        default:
            throw new AssertionError("Unknown rotation " + value + " for " + getHandle());
        }
    }

    public void setRotation(Rotation rotation) {
        Validate.notNull(rotation, "Rotation cannot be null");
        getHandle().setRotation(toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        // Translate Bukkit API rotation to NMS integer
        switch (rotation) {
        case NONE:
            return 0;
        case CLOCKWISE:
            return 1;
        case FLIPPED:
            return 2;
        case COUNTER_CLOCKWISE:
            return 3;
        default:
            throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
        }
    }

    @Override
    public EntityItemFrame getHandle() {
        return (EntityItemFrame) entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }

    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
