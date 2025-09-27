package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.InstrumentComponent;
import org.bukkit.MusicInstrument;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaMusicInstrument extends CraftMetaItem implements MusicInstrumentMeta {

    static final ItemMetaKeyType<InstrumentComponent> GOAT_HORN_INSTRUMENT = new ItemMetaKeyType<>(DataComponents.INSTRUMENT, "instrument");
    private MusicInstrument instrument;

    CraftMetaMusicInstrument(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof final CraftMetaMusicInstrument musicInstrumentMeta) {
            this.instrument = musicInstrumentMeta.instrument;
        }
    }

    CraftMetaMusicInstrument(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper

        getOrEmpty(tag, CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT).ifPresent((en) -> {
            en.instrument().unwrap(CraftRegistry.getMinecraftRegistry())
                .ifPresent(instrument -> this.instrument = CraftMusicInstrument.minecraftHolderToBukkit(instrument));
        });
    }

    CraftMetaMusicInstrument(Map<String, Object> map) {
        super(map);

        Object instrumentString = SerializableMeta.getObject(Object.class, map, CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.BUKKIT, true); // Paper - switch to Holder
        if (instrumentString != null) {
            this.instrument = CraftMusicInstrument.stringToBukkit(instrumentString);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.instrument != null) {
            tag.put(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT, new InstrumentComponent(CraftMusicInstrument.bukkitToMinecraftHolder(this.instrument)));
        }
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaMusicInstrument other) {
            return this.instrument == other.instrument;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaMusicInstrument || this.isInstrumentEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isInstrumentEmpty();
    }

    boolean isInstrumentEmpty() {
        return this.instrument == null;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasInstrument()) {
            hash = 61 * hash + this.instrument.hashCode();
        }

        return original != hash ? CraftMetaMusicInstrument.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaMusicInstrument clone() {
        CraftMetaMusicInstrument meta = (CraftMetaMusicInstrument) super.clone();
        meta.instrument = this.instrument;
        return meta;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasInstrument()) {
            builder.put(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.BUKKIT, CraftMusicInstrument.bukkitToString(this.instrument));
        }

        return builder;
    }

    @Override
    public MusicInstrument getInstrument() {
        return this.instrument;
    }

    public boolean hasInstrument() {
        return this.instrument != null;
    }

    @Override
    public void setInstrument(MusicInstrument instrument) {
        this.instrument = instrument;
    }
}
