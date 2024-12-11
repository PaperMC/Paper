package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaMusicInstrument extends CraftMetaItem implements MusicInstrumentMeta {

    static final ItemMetaKeyType<Holder<Instrument>> GOAT_HORN_INSTRUMENT = new ItemMetaKeyType<>(DataComponents.INSTRUMENT, "instrument");
    private MusicInstrument instrument;

    CraftMetaMusicInstrument(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaMusicInstrument) {
            CraftMetaMusicInstrument craftMetaMusicInstrument = (CraftMetaMusicInstrument) meta;
            this.instrument = craftMetaMusicInstrument.instrument;
        }
    }

    CraftMetaMusicInstrument(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT).ifPresent((instrument) -> {
            this.instrument = CraftMusicInstrument.minecraftHolderToBukkit(instrument);
        });
    }

    CraftMetaMusicInstrument(Map<String, Object> map) {
        super(map);

        String instrumentString = SerializableMeta.getString(map, CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.BUKKIT, true);
        if (instrumentString != null) {
            this.instrument = CraftMusicInstrument.stringToBukkit(instrumentString);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.instrument != null) {
            tag.put(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT, CraftMusicInstrument.bukkitToMinecraftHolder(this.instrument));
        }
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaMusicInstrument) {
            CraftMetaMusicInstrument that = (CraftMetaMusicInstrument) meta;
            return this.instrument == that.instrument;
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
        final int orginal;
        int hash = orginal = super.applyHash();

        if (this.hasInstrument()) {
            hash = 61 * hash + this.instrument.hashCode();
        }

        return orginal != hash ? CraftMetaMusicInstrument.class.hashCode() ^ hash : hash;
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
