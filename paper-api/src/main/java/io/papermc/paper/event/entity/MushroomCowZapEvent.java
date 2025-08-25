package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.MushroomCow;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Stores data for mushroom cow being zapped
 */
@NullMarked
public class MushroomCowZapEvent extends EntityZapEvent implements Cancellable {

    private final MushroomCow.Variant variant;

    @ApiStatus.Internal
    public MushroomCowZapEvent(final MushroomCow entity, final LightningStrike bolt, final MushroomCow.Variant variant) {
        super(entity, bolt, entity);
        this.variant = variant;
    }

    /**
     * Get the variant to set for the affected mushroom cow in this event.
     *
     * @return the variant replacement
     */
    public MushroomCow.Variant getVariant() {
        return this.variant;
    }
}
