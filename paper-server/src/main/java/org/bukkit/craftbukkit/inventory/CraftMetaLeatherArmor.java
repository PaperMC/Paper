package org.bukkit.craftbukkit.inventory;

import static org.bukkit.craftbukkit.inventory.CraftItemFactory.*;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaLeatherArmor extends CraftMetaItem implements LeatherArmorMeta {

    private static final Set<Material> LEATHER_ARMOR_MATERIALS = Sets.newHashSet(
            Material.LEATHER_HELMET,
            Material.LEATHER_HORSE_ARMOR,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.WOLF_ARMOR
    );

    static final ItemMetaKeyType<DyedItemColor> COLOR = new ItemMetaKeyType<>(DataComponents.DYED_COLOR, "color");

    private Color color = DEFAULT_LEATHER_COLOR;

    CraftMetaLeatherArmor(CraftMetaItem meta) {
        super(meta);
        readColor(this, meta);
    }

    CraftMetaLeatherArmor(DataComponentPatch tag) {
        super(tag);
        readColor(this, tag);
    }

    CraftMetaLeatherArmor(Map<String, Object> map) {
        super(map);
        readColor(this, map);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);
        applyColor(this, itemTag);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isLeatherArmorEmpty();
    }

    boolean isLeatherArmorEmpty() {
        return !(hasColor());
    }

    @Override
    boolean applicableTo(Material type) {
        return LEATHER_ARMOR_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaLeatherArmor clone() {
        return (CraftMetaLeatherArmor) super.clone();
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? DEFAULT_LEATHER_COLOR : color;
    }

    boolean hasColor() {
        return hasColor(this);
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        serialize(this, builder);

        return builder;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaLeatherArmor) {
            CraftMetaLeatherArmor that = (CraftMetaLeatherArmor) meta;

            return color.equals(that.color);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaLeatherArmor || isLeatherArmorEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasColor()) {
            hash ^= color.hashCode();
        }
        return original != hash ? CraftMetaLeatherArmor.class.hashCode() ^ hash : hash;
    }

    static void readColor(LeatherArmorMeta meta, CraftMetaItem other) {
        if (!(other instanceof CraftMetaLeatherArmor armorMeta)) {
            return;
        }

        meta.setColor(armorMeta.color);
    }

    static void readColor(LeatherArmorMeta meta, DataComponentPatch tag) {
        getOrEmpty(tag, COLOR).ifPresent((dyedItemColor) -> {
            if (!dyedItemColor.showInTooltip()) {
                meta.addItemFlags(ItemFlag.HIDE_DYE);
            }

            try {
                meta.setColor(Color.fromRGB(dyedItemColor.rgb()));
            } catch (IllegalArgumentException ex) {
                // Invalid colour
            }
        });
    }

    static void readColor(LeatherArmorMeta meta, Map<String, Object> map) {
        meta.setColor(SerializableMeta.getObject(Color.class, map, COLOR.BUKKIT, true));
    }

    static boolean hasColor(LeatherArmorMeta meta) {
        return !DEFAULT_LEATHER_COLOR.equals(meta.getColor());
    }

    static void applyColor(LeatherArmorMeta meta, CraftMetaItem.Applicator tag) {
        if (hasColor(meta)) {
            tag.put(COLOR, new DyedItemColor(meta.getColor().asRGB(), !meta.hasItemFlag(ItemFlag.HIDE_DYE)));
        }
    }

    static void serialize(LeatherArmorMeta meta, Builder<String, Object> builder) {
        if (hasColor(meta)) {
            builder.put(COLOR.BUKKIT, meta.getColor());
        }
    }
}
