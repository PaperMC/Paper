package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class CraftItemStack extends ItemStack {
    protected net.minecraft.server.ItemStack item;

    public CraftItemStack(net.minecraft.server.ItemStack item) {
        super(
            item != null ? item.id: 0,
            item != null ? item.count : 0,
            (short)(item != null ? item.damage : 0)
        );
        this.item = item;
    }

    /* 'Overwritten' constructors from ItemStack, yay for Java sucking */
    public CraftItemStack(final int type) {
        this(type, 0);
    }

    public CraftItemStack(final Material type) {
        this(type, 0);
    }

    public CraftItemStack(final int type, final int amount) {
        this(type, amount, (byte) 0);
    }

    public CraftItemStack(final Material type, final int amount) {
        this(type.getId(), amount);
    }

    public CraftItemStack(final int type, final int amount, final byte damage) {
        this(type, amount, damage, null);
    }

    public CraftItemStack(final Material type, final int amount, final byte damage) {
        this(type.getId(), amount, damage);
    }

    public CraftItemStack(final Material type, final int amount, final byte damage, final Byte data) {
        this(type.getId(), amount, damage, data);
    }

    public CraftItemStack(int type, int amount, byte damage, Byte data) {
        this(new net.minecraft.server.ItemStack(type, amount, data != null ? data : damage));
    }

    /*
     * Unsure if we have to sync before each of these calls the values in 'item'
     * are all public.
     */

    @Override
    public Material getType() {
        super.setTypeId(item != null ? item.id : 0); // sync, needed?
        return super.getType();
    }

    @Override
    public int getTypeId() {
        super.setTypeId(item != null ? item.id : 0); // sync, needed?
        return item != null ? item.id : 0;
    }

    @Override
    public void setTypeId(int type) {
        if (type == 0) {
            super.setTypeId(0);
            super.setAmount(0);
            item = null;
        } else {
            if (item == null) {
                item = new net.minecraft.server.ItemStack(type, 1, 0);
                super.setAmount(1);
            } else {
                item.id = type;
                super.setTypeId(item.id);
            }
        }
    }

    @Override
    public int getAmount() {
        super.setAmount(item != null ? item.count : 0); // sync, needed?
        return (item != null ? item.count : 0);
    }

    @Override
    public void setAmount(int amount) {
        if (amount == 0) {
            super.setTypeId(0);
            super.setAmount(0);
            item = null;
        } else {
            super.setAmount(amount);
            item.count = amount;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (item != null) {
            super.setDurability(durability);
            item.damage = durability;
        }
    }

    @Override
    public short getDurability() {
        if (item != null) {
            super.setDurability((short) item.damage); // sync, needed?
            return (short) item.damage;
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return item.a().b();
    }
}
