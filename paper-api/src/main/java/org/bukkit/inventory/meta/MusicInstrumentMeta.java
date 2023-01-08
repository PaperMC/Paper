package org.bukkit.inventory.meta;

import org.bukkit.MusicInstrument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MusicInstrumentMeta extends ItemMeta {

    /**
     * Sets the goat horn's instrument.
     *
     * @param instrument the instrument to set
     */
    void setInstrument(@Nullable MusicInstrument instrument);

    /**
     * Gets the instrument of the goat horn.
     *
     * @return The instrument of the goat horn
     */
    @Nullable
    MusicInstrument getInstrument();

    @Override
    @NotNull
    MusicInstrumentMeta clone();
}
