package org.bukkit.craftbukkit;

import org.bukkit.ItemStack;
import org.bukkit.Material;

public class CraftItemStack extends ItemStack {
    protected net.minecraft.server.ItemStack item;

    public CraftItemStack(net.minecraft.server.ItemStack item) {
        super(item != null ? item.c : 0, item != null ? item.a : 0);
        this.item = item;
    }

    /*
     * Unsure if we have to sync before each of these calls the values in 'item'
     * are all public.
     */

    @Override
    public Material getType() {
        super.setTypeID(item != null ? item.c : 0); // sync, needed?
        return super.getType();
    }

    @Override
    public int getTypeID() {
        super.setTypeID(item != null ? item.c : 0); // sync, needed?
        return item != null ? item.c : 0;
    }

    @Override
    public void setTypeID(int type) {
        if (type == 0) {
            super.setTypeID(0);
            super.setAmount(0);
            item = null;
        } else {
            if (item == null) {
                item = new net.minecraft.server.ItemStack(type);
                super.setAmount(1);
            } else {
                item.c = type;
                super.setTypeID(item.c);
            }
        }
    }

    @Override
    public int getAmount() {
        super.setAmount(item != null ? item.a : 0); // sync, needed?
        return (item != null ? item.a : 0);
    }

    @Override
    public void setAmount(int amount) {
        if (amount == 0) {
            super.setTypeID(0);
            super.setAmount(0);
            item = null;
        } else {
            super.setAmount(amount);
            item.a = amount;
        }
    }

    @Override
    public void setDamage(final byte damage) {
        // Ignore damage if item is null
        if (item != null) {
            super.setDamage(damage);
            item.d = damage;
        } 
    }

    @Override
    public byte getDamage() {
        if (item != null) {
            super.setDamage((byte) item.d); // sync, needed?    
            return (byte) item.d;
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxStackSize() {
        return item.a().b();
    }
}
