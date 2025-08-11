package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.MushroomCow;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Stores data for mushroom cow being zapped
 */
public class MushroomCowZapEvent extends EntityZapEvent implements Cancellable {

    private MushroomCow.Variant variant;

    @ApiStatus.Internal
    public MushroomCowZapEvent(final MushroomCow entity, final LightningStrike bolt, final MushroomCow.Variant variantReplacement) {
        super(entity, bolt, entity);
        this.variant = variantReplacement;
    }

    /**
     * Retrieves the variant replacement for the affected mushroom cow in this event.
     *
     * @return the variant replacement
     */
    public MushroomCow.Variant getVariantReplacement() {
        return this.variant;
    }
}
