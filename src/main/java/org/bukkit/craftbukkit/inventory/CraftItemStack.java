package org.bukkit.craftbukkit.inventory;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(ItemStack.class)
public class CraftItemStack extends ItemStack {
    protected net.minecraft.server.ItemStack item;

    public CraftItemStack(net.minecraft.server.ItemStack item) {
        super(
            item != null ? item.id: 0,
            item != null ? item.count : 0,
            (short)(item != null ? item.getData() : 0)
        );
        this.item = item;
    }

    public CraftItemStack(ItemStack item) {
        this(item.getTypeId(), item.getAmount(), item.getDurability());
        addUnsafeEnchantments(item.getEnchantments());
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

    public CraftItemStack(final int type, final int amount, final short damage) {
        this(type, amount, damage, null);
    }

    public CraftItemStack(final Material type, final int amount, final short damage) {
        this(type.getId(), amount, damage);
    }

    public CraftItemStack(final Material type, final int amount, final short damage, final Byte data) {
        this(type.getId(), amount, damage, data);
    }

    public CraftItemStack(int type, int amount, short damage, Byte data) {
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
            item.b(durability);
        }
    }

    @Override
    public short getDurability() {
        if (item != null) {
            super.setDurability((short) item.getData()); // sync, needed?
            return (short) item.getData();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return item.getItem().getMaxStackSize();
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Map<Enchantment, Integer> enchantments = getEnchantments();
        enchantments.put(ench, level);
        rebuildEnchantments(enchantments);
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        return EnchantmentManager.b(ench.getId(), item);
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        Map<Enchantment, Integer> enchantments = getEnchantments();
        Integer previous = enchantments.remove(ench);

        rebuildEnchantments(enchantments);

        return (previous == null) ? 0 : previous;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> result = new HashMap<Enchantment, Integer>();
        NBTTagList list = item.p();

        if (list == null) {
            return result;
        }

        for (int i = 0; i < list.d(); i++) {
            short id = ((NBTTagCompound)list.a(i)).e("id");
            short level = ((NBTTagCompound)list.a(i)).e("lvl");

            result.put(Enchantment.getById(id), (int)level);
        }

        return result;
    }

    private void rebuildEnchantments(Map<Enchantment, Integer> enchantments) {
        NBTTagCompound tag = item.tag;
        NBTTagList list = new NBTTagList("ench");

        if (tag == null) {
            tag = item.tag = new NBTTagCompound();
        }

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            NBTTagCompound subtag = new NBTTagCompound();

            subtag.a("id", (short)entry.getKey().getId());
            subtag.a("lvl", (short)(int)entry.getValue());

            list.a(subtag);
        }

        tag.a("ench", (NBTBase)list);
    }

    public net.minecraft.server.ItemStack getHandle() {
        return item;
    }

    public static net.minecraft.server.ItemStack createNMSItemStack(ItemStack original) {
        return new CraftItemStack(original).getHandle();
    }
}
