package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaArmor extends CraftMetaItem implements ArmorMeta {

    private static final Set<Material> ARMOR_MATERIALS = Sets.newHashSet(
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.TURTLE_HELMET
    );

    static final ItemMetaKeyType<net.minecraft.world.item.armortrim.ArmorTrim> TRIM = new ItemMetaKeyType<>(DataComponents.TRIM, "trim");
    static final ItemMetaKey TRIM_MATERIAL = new ItemMetaKey("material");
    static final ItemMetaKey TRIM_PATTERN = new ItemMetaKey("pattern");

    private ArmorTrim trim;

    CraftMetaArmor(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaArmor armorMeta) {
            this.trim = armorMeta.trim;
        }
    }

    CraftMetaArmor(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, TRIM).ifPresent((trimCompound) -> {
            TrimMaterial trimMaterial = CraftTrimMaterial.minecraftHolderToBukkit(trimCompound.material());
            TrimPattern trimPattern = CraftTrimPattern.minecraftHolderToBukkit(trimCompound.pattern());

            this.trim = new ArmorTrim(trimMaterial, trimPattern);

            if (!trimCompound.showInTooltip) {
                addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            }
        });
    }

    CraftMetaArmor(Map<String, Object> map) {
        super(map);

        Map<?, ?> trimData = SerializableMeta.getObject(Map.class, map, TRIM.BUKKIT, true);
        if (trimData != null) {
            String materialKeyString = SerializableMeta.getString(trimData, TRIM_MATERIAL.BUKKIT, true);
            String patternKeyString = SerializableMeta.getString(trimData, TRIM_PATTERN.BUKKIT, true);

            if (materialKeyString != null && patternKeyString != null) {
                NamespacedKey materialKey = NamespacedKey.fromString(materialKeyString);
                NamespacedKey patternKey = NamespacedKey.fromString(patternKeyString);

                if (materialKey != null && patternKey != null) {
                    TrimMaterial trimMaterial = Registry.TRIM_MATERIAL.get(materialKey);
                    TrimPattern trimPattern = Registry.TRIM_PATTERN.get(patternKey);

                    if (trimMaterial != null && trimPattern != null) {
                        this.trim = new ArmorTrim(trimMaterial, trimPattern);
                    }
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);

        if (hasTrim()) {
            itemTag.put(TRIM, new net.minecraft.world.item.armortrim.ArmorTrim(CraftTrimMaterial.bukkitToMinecraftHolder(trim.getMaterial()), CraftTrimPattern.bukkitToMinecraftHolder(trim.getPattern()), !hasItemFlag(ItemFlag.HIDE_ARMOR_TRIM)));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return ARMOR_MATERIALS.contains(type);
    }

    @Override
    boolean equalsCommon(CraftMetaItem that) {
        if (!super.equalsCommon(that)) {
            return false;
        }

        if (that instanceof CraftMetaArmor armorMeta) {
            return Objects.equals(trim, armorMeta.trim);
        }

        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmor || isArmorEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isArmorEmpty();
    }

    private boolean isArmorEmpty() {
        return !hasTrim();
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasTrim()) {
            hash = 61 * hash + trim.hashCode();
        }

        return original != hash ? CraftMetaArmor.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaArmor clone() {
        CraftMetaArmor meta = (CraftMetaArmor) super.clone();
        meta.trim = this.trim;
        return meta;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasTrim()) {
            Map<String, String> trimData = new HashMap<>();
            trimData.put(TRIM_MATERIAL.BUKKIT, trim.getMaterial().getKey().toString());
            trimData.put(TRIM_PATTERN.BUKKIT, trim.getPattern().getKey().toString());
            builder.put(TRIM.BUKKIT, trimData);
        }

        return builder;
    }

    @Override
    public boolean hasTrim() {
        return trim != null;
    }

    @Override
    public void setTrim(ArmorTrim trim) {
        this.trim = trim;
    }

    @Override
    public ArmorTrim getTrim() {
        return trim;
    }
}
