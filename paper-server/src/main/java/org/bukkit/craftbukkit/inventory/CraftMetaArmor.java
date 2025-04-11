package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaArmor extends CraftMetaItem implements ArmorMeta {

    static final ItemMetaKeyType<net.minecraft.world.item.equipment.trim.ArmorTrim> TRIM = new ItemMetaKeyType<>(DataComponents.TRIM, "trim");
    static final ItemMetaKey TRIM_MATERIAL = new ItemMetaKey("material");
    static final ItemMetaKey TRIM_PATTERN = new ItemMetaKey("pattern");

    private ArmorTrim trim;

    CraftMetaArmor(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaArmor armorMeta) {
            this.trim = armorMeta.trim;
        }
    }

    CraftMetaArmor(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaArmor.TRIM).ifPresent((trimCompound) -> {
            TrimMaterial trimMaterial = CraftTrimMaterial.minecraftHolderToBukkit(trimCompound.material());
            TrimPattern trimPattern = CraftTrimPattern.minecraftHolderToBukkit(trimCompound.pattern());

            this.trim = new ArmorTrim(trimMaterial, trimPattern);
        });
    }

    CraftMetaArmor(Map<String, Object> map) {
        super(map);

        Map<?, ?> trimData = SerializableMeta.getObject(Map.class, map, CraftMetaArmor.TRIM.BUKKIT, true);
        if (trimData != null) {
            Object materialKeyString = SerializableMeta.getObject(Object.class, trimData, CraftMetaArmor.TRIM_MATERIAL.BUKKIT, true); // Paper - switch to Holder
            Object patternKeyString = SerializableMeta.getObject(Object.class, trimData, CraftMetaArmor.TRIM_PATTERN.BUKKIT, true); // Paper - switch to Holder

            if (materialKeyString != null && patternKeyString != null) {
                // Paper start - switch to Holder
                TrimMaterial trimMaterial = CraftTrimMaterial.objectToBukkit(materialKeyString);
                TrimPattern trimPattern = CraftTrimPattern.objectToBukkit(patternKeyString);
                if (trimMaterial != null && trimPattern != null) {
                    this.trim = new ArmorTrim(trimMaterial, trimPattern);
                }
                // Paper end - switch to Holder
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasTrim()) {
            tag.put(CraftMetaArmor.TRIM, new net.minecraft.world.item.equipment.trim.ArmorTrim(CraftTrimMaterial.bukkitToMinecraftHolder(this.trim.getMaterial()), CraftTrimPattern.bukkitToMinecraftHolder(this.trim.getPattern())));
        }
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }

        if (meta instanceof CraftMetaArmor other) {
            return Objects.equals(this.trim, other.trim);
        }

        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmor || this.isArmorEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isArmorEmpty();
    }

    private boolean isArmorEmpty() {
        return !this.hasTrim();
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasTrim()) {
            hash = 61 * hash + this.trim.hashCode();
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

        if (this.hasTrim()) {
            Map<String, Object> trimData = new HashMap<>(); // Paper - switch to Holder
            trimData.put(CraftMetaArmor.TRIM_MATERIAL.BUKKIT, CraftTrimMaterial.bukkitToObject(this.trim.getMaterial())); // Paper - switch to Holder
            trimData.put(CraftMetaArmor.TRIM_PATTERN.BUKKIT, CraftTrimPattern.bukkitToObject(this.trim.getPattern())); // Paper - switch to Holder
            builder.put(CraftMetaArmor.TRIM.BUKKIT, trimData);
        }

        return builder;
    }

    @Override
    public boolean hasTrim() {
        return this.trim != null;
    }

    @Override
    public void setTrim(ArmorTrim trim) {
        this.trim = trim;
    }

    @Override
    public ArmorTrim getTrim() {
        return this.trim;
    }
}
