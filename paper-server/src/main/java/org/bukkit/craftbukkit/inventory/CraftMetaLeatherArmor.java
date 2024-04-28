package org.bukkit.craftbukkit.inventory;

import static org.bukkit.craftbukkit.inventory.CraftItemFactory.*;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaLeatherArmor extends CraftMetaItem implements LeatherArmorMeta {

    static final ItemMetaKeyType<DyedItemColor> COLOR = new ItemMetaKeyType<>(DataComponents.DYED_COLOR, "color");

    private Integer color; // Paper - keep color component consistent with vanilla (top byte is ignored)

    CraftMetaLeatherArmor(CraftMetaItem meta) {
        super(meta);
        // Paper start
        if (!(meta instanceof CraftMetaLeatherArmor leatherMeta)) {
            return;
        }

        this.color = leatherMeta.color;
        // Paper end
    }

    CraftMetaLeatherArmor(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper
        // Paper start
        getOrEmpty(tag, CraftMetaLeatherArmor.COLOR).ifPresent((dyedItemColor) -> {
            if (!dyedItemColor.showInTooltip()) {
                this.addItemFlags(ItemFlag.HIDE_DYE);
            }

            this.color = dyedItemColor.rgb();
        });
        // Paper end
    }

    CraftMetaLeatherArmor(Map<String, Object> map) {
        super(map);
        CraftMetaLeatherArmor.readColor(this, map);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);
        // Paper start
        if (this.hasColor()) {
            itemTag.put(CraftMetaLeatherArmor.COLOR, new DyedItemColor(this.color, !this.hasItemFlag(ItemFlag.HIDE_DYE)));
        }
        // Paper end
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isLeatherArmorEmpty();
    }

    boolean isLeatherArmorEmpty() {
        return !(this.hasColor());
    }

    @Override
    boolean applicableTo(Material type) {
        if (!type.isItem()) {
            return false;
        }

        return type.asItemType().getItemMetaClass() == LeatherArmorMeta.class || type.asItemType().getItemMetaClass() == ColorableArmorMeta.class;
    }

    @Override
    public CraftMetaLeatherArmor clone() {
        return (CraftMetaLeatherArmor) super.clone();
    }

    @Override
    public Color getColor() {
        return this.color == null ? DEFAULT_LEATHER_COLOR : Color.fromRGB(this.color & 0xFFFFFF); // Paper
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? null : color.asRGB(); // Paper
    }

    boolean hasColor() {
        return this.color != null; // Paper
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        CraftMetaLeatherArmor.serialize(this, builder);

        return builder;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaLeatherArmor) {
            CraftMetaLeatherArmor that = (CraftMetaLeatherArmor) meta;

            return this.hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor(); // Paper - allow null
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaLeatherArmor || this.isLeatherArmorEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasColor()) {
            hash ^= this.color.hashCode();
        }
        return original != hash ? CraftMetaLeatherArmor.class.hashCode() ^ hash : hash;
    }

    @io.papermc.paper.annotation.DoNotUse // Paper
    static void readColor(LeatherArmorMeta meta, CraftMetaItem other) {
        if (!(other instanceof CraftMetaLeatherArmor armorMeta)) {
            return;
        }

        // meta.setColor(armorMeta.color); // Paper - commented out, color is now an integer and cannot be passed to setColor
    }

    @io.papermc.paper.annotation.DoNotUse // Paper
    static void readColor(LeatherArmorMeta meta, DataComponentPatch tag) {
        getOrEmpty(tag, CraftMetaLeatherArmor.COLOR).ifPresent((dyedItemColor) -> {
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
        meta.setColor(SerializableMeta.getObject(Color.class, map, CraftMetaLeatherArmor.COLOR.BUKKIT, true));
    }

    static boolean hasColor(LeatherArmorMeta meta) {
        return !DEFAULT_LEATHER_COLOR.equals(meta.getColor());
    }

    @io.papermc.paper.annotation.DoNotUse // Paper
    static void applyColor(LeatherArmorMeta meta, CraftMetaItem.Applicator tag) {
        if (CraftMetaLeatherArmor.hasColor(meta)) {
            tag.put(CraftMetaLeatherArmor.COLOR, new DyedItemColor(meta.getColor().asRGB(), !meta.hasItemFlag(ItemFlag.HIDE_DYE)));
        }
    }

    static void serialize(LeatherArmorMeta meta, Builder<String, Object> builder) {
        if (CraftMetaLeatherArmor.hasColor(meta)) {
            builder.put(CraftMetaLeatherArmor.COLOR.BUKKIT, meta.getColor());
        }
    }

    // Paper start - Expose #hasColor to leather armor
    @Override
    public boolean isDyed() {
        return hasColor();
    }
    // Paper end - Expose #hasColor to leather armor
}
