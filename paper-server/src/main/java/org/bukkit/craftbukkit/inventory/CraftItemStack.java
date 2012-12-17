package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import org.apache.commons.lang.Validate;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.ImmutableMap;

@DelegateDeserialization(ItemStack.class)
public final class CraftItemStack extends ItemStack {

    public static net.minecraft.server.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return stack.handle == null ? null : stack.handle.cloneItemStack();
        }
        if (original == null || original.getTypeId() <= 0) {
            return null;
        }
        net.minecraft.server.ItemStack stack = new net.minecraft.server.ItemStack(original.getTypeId(), original.getAmount(), original.getDurability());
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static net.minecraft.server.ItemStack copyNMSStack(net.minecraft.server.ItemStack original, int amount) {
        net.minecraft.server.ItemStack stack = original.cloneItemStack();
        stack.count = amount;
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.server.ItemStack original) {
        if (original == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(original.id, original.count, (short) original.getData());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        return stack;
    }

    public static CraftItemStack asCraftMirror(net.minecraft.server.ItemStack original) {
        return new CraftItemStack(original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return new CraftItemStack(stack.handle == null ? null : stack.handle.cloneItemStack());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(net.minecraft.server.Item item) {
        return asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(net.minecraft.server.Item item, int amount) {
        return new CraftItemStack(item.id, amount, (short) 0, null);
    }

    net.minecraft.server.ItemStack handle;

    /**
     * Mirror
     */
    private CraftItemStack(net.minecraft.server.ItemStack item) {
        this.handle = item;
    }

    private CraftItemStack(ItemStack item) {
        this(item.getTypeId(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(int typeId, int amount, short durability, ItemMeta itemMeta) {
        setTypeId(typeId);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    @Override
    public int getTypeId() {
        return handle != null ? handle.id : 0;
    }

    @Override
    public void setTypeId(int type) {
        if (getTypeId() == type) {
            return;
        } else if (type == 0) {
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.server.ItemStack(type, 1, 0);
        } else {
            handle.id = type;
            if (hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                setItemMeta(handle, getItemMeta(handle));
            }
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.count : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) {
            return;
        }
        if (amount == 0) {
            handle = null;
        } else {
            handle.count = amount;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (handle != null) {
            handle.setData(durability);
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short) handle.getData();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return (handle == null) ? Material.AIR.getMaxStackSize() : handle.getItem().getMaxStackSize();
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Validate.notNull(ench, "Cannot add null enchantment");

        if (!makeTag(handle)) {
            return;
        }
        NBTTagList list = getEnchantmentList(handle), listCopy;
        if (list == null) {
            list = new NBTTagList("ench");
            handle.tag.set("ench", list);
        }
        int size = list.size();

        for (int i = 0; i < size; i++) {
            NBTTagCompound tag = (NBTTagCompound) list.get(i);
            short id = tag.getShort("id");
            if (id == ench.getId()) {
                tag.setShort("lvl", (short) level);
                return;
            }
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", (short) ench.getId());
        tag.setShort("lvl", (short) level);
        list.add(tag);
    }

    static boolean makeTag(net.minecraft.server.ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.tag != null) {
            return true;
        }
        item.tag = new NBTTagCompound();
        return true;
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        Validate.notNull(ench, "Cannot find null enchantment");
        if (handle == null) {
            return 0;
        }
        return EnchantmentManager.getEnchantmentLevel(ench.getId(), handle);
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        Validate.notNull(ench, "Cannot remove null enchantment");

        NBTTagList list = getEnchantmentList(handle), listCopy;
        if (list == null) {
            return 0;
        }
        int index = Integer.MIN_VALUE, size = list.size(), level;

        for (int i = 0; i < size; i++) {
            short id = ((NBTTagCompound) list.get(i)).getShort("id");
            if (id == ench.getId()) {
                index = i;
                break;
            }
        }

        if (index == Integer.MIN_VALUE) {
            return 0;
        }
        if (index == 0 && size == 0) {
            handle.tag.o("ench");
            if (handle.tag.d()) {
                handle.tag = null;
            }
        }

        listCopy = new NBTTagList("ench");
        level = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                level = ((NBTTagCompound) list.get(i)).getShort("id");
                continue;
            }
            listCopy.add(list.get(i));
        }
        handle.tag.set("ench", listCopy);
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return getEnchantments(handle);
    }

    static Map<Enchantment, Integer> getEnchantments(net.minecraft.server.ItemStack item) {
        ImmutableMap.Builder<Enchantment, Integer> result = ImmutableMap.builder();
        NBTTagList list = (item == null) ? null : item.getEnchantments();

        if (list == null) {
            return result.build();
        }

        for (int i = 0; i < list.size(); i++) {
            short id = ((NBTTagCompound) list.get(i)).getShort("id");
            short level = ((NBTTagCompound) list.get(i)).getShort("lvl");

            result.put(Enchantment.getById(id), (int) level);
        }

        return result.build();
    }

    static NBTTagList getEnchantmentList(net.minecraft.server.ItemStack item) {
        return item == null ? null : item.getEnchantments();
    }

    @Override
    public CraftItemStack clone() {
        CraftItemStack itemStack = (CraftItemStack) super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.cloneItemStack();
        }
        return itemStack;
    }

    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(handle);
    }

    static ItemMeta getItemMeta(net.minecraft.server.ItemStack item) {
        if (!hasItemMeta(item)) {
            return CraftItemFactory.instance().getItemMeta(getType(item));
        }
        switch (getType(item)) {
            case WRITTEN_BOOK:
            case BOOK_AND_QUILL:
                return new CraftMetaBook(item.tag);
            case SKULL_ITEM:
                return new CraftMetaSkull(item.tag);
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return new CraftMetaLeatherArmor(item.tag);
            case POTION:
                return new CraftMetaPotion(item.tag);
            case MAP:
                return new CraftMetaMap(item.tag);
            default:
                return new CraftMetaItem(item.tag);
        }
    }

    static Material getType(net.minecraft.server.ItemStack item) {
        Material material = Material.getMaterial(item == null ? 0 : item.id);
        return material == null ? Material.AIR : material;
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta(handle, itemMeta);
    }

    static boolean setItemMeta(net.minecraft.server.ItemStack item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (itemMeta == null) {
            item.tag = null;
            return true;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, getType(item))) {
            return false;
        }

        NBTTagCompound tag = new NBTTagCompound();
        item.setTag(tag);

        ((CraftMetaItem) itemMeta).applyToItem(tag);
        return true;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof CraftItemStack)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }

        CraftItemStack that = (CraftItemStack) stack;
        if (handle == that.handle) {
            return true;
        }
        if (handle == null || that.handle == null) {
            return false;
        }
        if (!(that.getTypeId() == getTypeId() && getDurability() == that.getDurability())) {
            return false;
        }
        return hasItemMeta() ? that.hasItemMeta() && handle.tag.equals(that.handle.tag) : !that.hasItemMeta();
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta(handle);
    }

    static boolean hasItemMeta(net.minecraft.server.ItemStack item) {
        return !(item == null || item.tag == null || item.tag.d());
    }
}
