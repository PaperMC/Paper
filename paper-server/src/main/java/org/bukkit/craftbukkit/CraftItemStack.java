package org.bukkit.craftbukkit;

import org.bukkit.ItemStack;
import org.bukkit.Material;

public class CraftItemStack extends ItemStack {
    protected net.minecraft.server.ItemStack item;

    public CraftItemStack(net.minecraft.server.ItemStack item) {
        super(item.c, item.a);
        this.item = item;
    }

    /*
     * Unsure if we have to syn before each of these calls the values in 'item'
     * are all public.
     */

    @Override
    public Material getType() {
        super.setTypeID(item.c); // sync, needed?
        return super.getType();
    }

    @Override
    public int getTypeID() {
        super.setTypeID(item.c); // sync, needed?
        return item.c;
    }

    @Override
    public void setTypeID(int type) {
        super.setTypeID(item.c);
        item.c = type;
    }

    @Override
    public int getAmount() {
        super.setAmount(item.a); // sync, needed?
        return item.a;
    }

    @Override
    public void setAmount(int amount) {
        super.setAmount(amount);
        item.a = amount;
    }

    @Override
    public void setDamage(final byte damage) {
        super.setDamage(damage);
        item.d = damage;
    }

    @Override
    public byte getDamage() {
        super.setDamage((byte) item.d); // sync, needed?
        return (byte) item.d;
    }

}
