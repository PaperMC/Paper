package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.OminousBottleMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaOminousBottle extends CraftMetaItem implements OminousBottleMeta {

    static final ItemMetaKeyType<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER = new ItemMetaKeyType<>(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, "ominous-bottle-amplifier");
    private Integer ominousBottleAmplifier;

    CraftMetaOminousBottle(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof final CraftMetaOminousBottle ominousBottleMeta)) {
            return;
        }
        this.ominousBottleAmplifier = ominousBottleMeta.ominousBottleAmplifier;
    }

    CraftMetaOminousBottle(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);
        getOrEmpty(tag, CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER).ifPresent((amplifier) -> {
            this.ominousBottleAmplifier = amplifier.value();
        });
    }

    CraftMetaOminousBottle(Map<String, Object> map) {
        super(map);
        Integer ominousBottleAmplifier = SerializableMeta.getObject(Integer.class, map, CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER.BUKKIT, true);
        if (ominousBottleAmplifier != null) {
            this.setAmplifier(ominousBottleAmplifier);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasAmplifier()) {
            tag.put(CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(this.ominousBottleAmplifier));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBottleEmpty();
    }

    boolean isBottleEmpty() {
        return !(this.hasAmplifier());
    }

    @Override
    public CraftMetaOminousBottle clone() {
        CraftMetaOminousBottle clone = ((CraftMetaOminousBottle) super.clone());
        return clone;
    }

    @Override
    public boolean hasAmplifier() {
        return this.ominousBottleAmplifier != null;
    }

    @Override
    public int getAmplifier() {
        Preconditions.checkState(this.hasAmplifier(), "'ominous_bottle_amplifier' data component is absent. Check hasAmplifier first!");
        return this.ominousBottleAmplifier;
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
        if (this.hasAmplifier()) {
            hash = 61 * hash + this.ominousBottleAmplifier.hashCode();
        }

        return original != hash ? CraftMetaOminousBottle.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }

        if (meta instanceof final CraftMetaOminousBottle other) {
            return Objects.equals(this.ominousBottleAmplifier, other.ominousBottleAmplifier);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaOminousBottle || this.isBottleEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasAmplifier()) {
            builder.put(CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER.BUKKIT, this.ominousBottleAmplifier);
        }

        return builder;
    }
}
