package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaEnchantedBook extends CraftMetaItem implements EnchantmentStorageMeta {
    static final ItemMetaKeyType<ItemEnchantments> STORED_ENCHANTMENTS = new ItemMetaKeyType<>(DataComponents.STORED_ENCHANTMENTS, "stored-enchants");

    private Map<Enchantment, Integer> enchantments;

    CraftMetaEnchantedBook(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaEnchantedBook)) {
            return;
        }

        CraftMetaEnchantedBook that = (CraftMetaEnchantedBook) meta;

        if (that.hasEnchants()) {
            this.enchantments = new LinkedHashMap<Enchantment, Integer>(that.enchantments);
        }
    }

    CraftMetaEnchantedBook(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS).ifPresent((itemEnchantments) -> {
            this.enchantments = buildEnchantments(itemEnchantments);
            if (!itemEnchantments.showInTooltip) {
                this.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            }
        });
    }

    CraftMetaEnchantedBook(Map<String, Object> map) {
        super(map);

        this.enchantments = buildEnchantments(map, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);

        this.applyEnchantments(this.enchantments, itemTag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isEnchantedEmpty();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaEnchantedBook) {
            CraftMetaEnchantedBook that = (CraftMetaEnchantedBook) meta;

            return (this.hasStoredEnchants() ? that.hasStoredEnchants() && this.enchantments.equals(that.enchantments) : !that.hasStoredEnchants());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaEnchantedBook || this.isEnchantedEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasStoredEnchants()) {
            hash = 61 * hash + this.enchantments.hashCode();
        }

        return original != hash ? CraftMetaEnchantedBook.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaEnchantedBook clone() {
        CraftMetaEnchantedBook meta = (CraftMetaEnchantedBook) super.clone();

        if (this.enchantments != null) {
            meta.enchantments = new LinkedHashMap<Enchantment, Integer>(this.enchantments);
        }

        return meta;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        serializeEnchantments(this.enchantments, builder, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);

        return builder;
    }

    boolean isEnchantedEmpty() {
        return !this.hasStoredEnchants();
    }

    @Override
    public boolean hasStoredEnchant(Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.containsKey(ench);
    }

    @Override
    public int getStoredEnchantLevel(Enchantment ench) {
        Integer level = this.hasStoredEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getStoredEnchants() {
        return this.hasStoredEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.<Enchantment, Integer>of();
    }

    @Override
    public boolean addStoredEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        if (this.enchantments == null) {
            this.enchantments = new LinkedHashMap<Enchantment, Integer>(4);
        }

        if (ignoreRestrictions || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            Integer old = this.enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeStoredEnchant(Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.remove(ench) != null;
    }

    @Override
    public boolean hasStoredEnchants() {
        return !(this.enchantments == null || this.enchantments.isEmpty());
    }

    @Override
    public boolean hasConflictingStoredEnchant(Enchantment ench) {
        return checkConflictingEnchants(this.enchantments, ench);
    }
}
