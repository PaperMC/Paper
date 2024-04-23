package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.OminousBottleMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaOminousBottle extends CraftMetaItem implements OminousBottleMeta {

    static final ItemMetaKeyType<Integer> OMINOUS_BOTTLE_AMPLIFIER = new ItemMetaKeyType<>(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, "ominous-bottle-amplifier");
    private Integer ominousBottleAmplifier;

    CraftMetaOminousBottle(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaOminousBottle)) {
            return;
        }
        CraftMetaOminousBottle bottleMeta = (CraftMetaOminousBottle) meta;
        ominousBottleAmplifier = bottleMeta.ominousBottleAmplifier;
    }

    CraftMetaOminousBottle(DataComponentPatch tag) {
        super(tag);
        getOrEmpty(tag, OMINOUS_BOTTLE_AMPLIFIER).ifPresent((amplifier) -> {
            ominousBottleAmplifier = amplifier;
        });
    }

    CraftMetaOminousBottle(Map<String, Object> map) {
        super(map);
        Integer ominousBottleAmplifier = SerializableMeta.getObject(Integer.class, map, OMINOUS_BOTTLE_AMPLIFIER.BUKKIT, true);
        if (ominousBottleAmplifier != null) {
            setAmplifier(ominousBottleAmplifier);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (hasAmplifier()) {
            tag.put(OMINOUS_BOTTLE_AMPLIFIER, ominousBottleAmplifier);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBottleEmpty();
    }

    boolean isBottleEmpty() {
        return !(hasAmplifier());
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.OMINOUS_BOTTLE;
    }

    @Override
    public CraftMetaOminousBottle clone() {
        CraftMetaOminousBottle clone = ((CraftMetaOminousBottle) super.clone());
        return clone;
    }

    @Override
    public boolean hasAmplifier() {
        return ominousBottleAmplifier != null;
    }

    @Override
    public int getAmplifier() {
        return ominousBottleAmplifier;
    }

    @Override
    public void setAmplifier(int amplifier) {
        Preconditions.checkArgument(0 <= amplifier && amplifier <= 4, "Amplifier must be in range [0, 4]");
        this.ominousBottleAmplifier = amplifier;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasAmplifier()) {
            hash = 61 * hash + ominousBottleAmplifier.hashCode();
        }

        return original != hash ? CraftMetaOminousBottle.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaOminousBottle) {
            CraftMetaOminousBottle that = (CraftMetaOminousBottle) meta;

            return (hasAmplifier() ? that.hasAmplifier() && this.ominousBottleAmplifier.equals(that.ominousBottleAmplifier) : !that.hasAmplifier());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaOminousBottle || isBottleEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasAmplifier()) {
            builder.put(OMINOUS_BOTTLE_AMPLIFIER.BUKKIT, ominousBottleAmplifier);
        }

        return builder;
    }
}
