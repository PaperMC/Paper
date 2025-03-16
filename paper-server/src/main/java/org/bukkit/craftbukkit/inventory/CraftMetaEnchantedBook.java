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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaEnchantedBook extends CraftMetaItem implements EnchantmentStorageMeta {
    static final ItemMetaKeyType<ItemEnchantments> STORED_ENCHANTMENTS = new ItemMetaKeyType<>(DataComponents.STORED_ENCHANTMENTS, "stored-enchants");

    private Map<Enchantment, Integer> enchantments;

    CraftMetaEnchantedBook(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof final CraftMetaEnchantedBook enchantedBookMeta)) {
            return;
        }

        if (enchantedBookMeta.hasEnchants()) {
            this.enchantments = new LinkedHashMap<>(enchantedBookMeta.enchantments);
        }
    }

    CraftMetaEnchantedBook(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS).ifPresent((itemEnchantments) -> {
            this.enchantments = buildEnchantments(itemEnchantments);
        });
    }

    CraftMetaEnchantedBook(Map<String, Object> map) {
        super(map);

        this.enchantments = buildEnchantments(map, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        this.applyEnchantments(this.enchantments, tag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
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
        if (meta instanceof final CraftMetaEnchantedBook other) {
            return (this.hasStoredEnchants() ? other.hasStoredEnchants() && this.enchantments.equals(other.enchantments) : !other.hasStoredEnchants());
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
            meta.enchantments = new LinkedHashMap<>(this.enchantments);
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
    public boolean hasStoredEnchant(Enchantment enchant) {
        return this.hasStoredEnchants() && this.enchantments.containsKey(enchant);
    }

    @Override
    public int getStoredEnchantLevel(Enchantment enchant) {
        Integer level = this.hasStoredEnchants() ? this.enchantments.get(enchant) : null;
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
    public boolean addStoredEnchant(Enchantment enchant, int level, boolean ignoreRestrictions) {
        if (this.enchantments == null) {
            this.enchantments = new LinkedHashMap<>(4);
        }

        if (ignoreRestrictions || level >= enchant.getStartLevel() && level <= enchant.getMaxLevel()) {
            Integer old = this.enchantments.put(enchant, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeStoredEnchant(Enchantment enchant) {
        return this.hasStoredEnchants() && this.enchantments.remove(enchant) != null;
    }

    @Override
    public boolean hasStoredEnchants() {
        return !(this.enchantments == null || this.enchantments.isEmpty());
    }

    @Override
    public boolean hasConflictingStoredEnchant(Enchantment enchant) {
        return checkConflictingEnchants(this.enchantments, enchant);
    }
}
