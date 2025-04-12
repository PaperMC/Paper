package org.bukkit.craftbukkit.inventory;

import static org.bukkit.craftbukkit.inventory.CraftItemFactory.*;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.ColorableArmorMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaColorableArmor extends CraftMetaArmor implements ColorableArmorMeta {

    private Integer color; // Paper - keep color component consistent with vanilla (top byte is ignored)

    CraftMetaColorableArmor(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaColorableArmor armorMeta)) {
            return;
        }

        this.color = armorMeta.color;
    }

    CraftMetaColorableArmor(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);
        getOrEmpty(tag, CraftMetaLeatherArmor.COLOR).ifPresent((dyedItemColor) -> {
            this.color = dyedItemColor.rgb();
        });
    }

    CraftMetaColorableArmor(Map<String, Object> map) {
        super(map);
        CraftMetaLeatherArmor.readColor(this, map);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);
        if (this.hasColor()) {
            tag.put(CraftMetaLeatherArmor.COLOR, new net.minecraft.world.item.component.DyedItemColor(this.color));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isLeatherArmorEmpty();
    }

    boolean isLeatherArmorEmpty() {
        return !(this.hasColor());
    }

    @Override
    public CraftMetaColorableArmor clone() {
        CraftMetaColorableArmor clone = (CraftMetaColorableArmor) super.clone();
        clone.color = this.color;
        return clone;
    }

    @Override
    public Color getColor() {
        return this.color == null ? DEFAULT_LEATHER_COLOR : Color.fromRGB(this.color & 0x00FFFFFF); // Paper - this should really be nullable
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? null : color.asRGB();
    }

    boolean hasColor() {
        return this.color != null;
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
        if (meta instanceof final CraftMetaColorableArmor other) {
            return Objects.equals(this.color, other.color);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaColorableArmor || this.isLeatherArmorEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasColor()) {
            hash ^= this.color.hashCode();
        }
        return original != hash ? CraftMetaColorableArmor.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean isDyed() {
        return this.hasColor();
    }
}
