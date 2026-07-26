package org.bukkit.event.entity;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import org.bukkit.Warning;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for pigs being zapped
 *
 * @deprecated use {@link EntityZapEvent}
 */
@Deprecated(since = "26.2")
@Warning(reason = "This event has become obsolete, the more generic EntityZapEvent should be used instead.")
public class PigZapEvent extends EntityZapEvent {

    @ApiStatus.Internal
    public PigZapEvent(@NotNull final Pig pig, @NotNull final LightningStrike bolt, @NotNull final PigZombie zombifiedPiglin) {
        super(pig, bolt, zombifiedPiglin);
    }

    @NotNull
    @Override
    public Pig getEntity() {
        return (Pig) this.entity;
    }

    /**
     * Gets the bolt which is striking the pig.
     *
     * @return lightning entity
     * @deprecated use {@link EntityZapEvent#getBolt()}
     */
    @NotNull
    @Deprecated(since = "26.2")
    public LightningStrike getLightning() {
        return super.getBolt();
    }

    /**
     * Gets the zombified piglin that will replace the pig, provided the event is
     * not cancelled first.
     *
     * @return resulting entity
     * @deprecated use {@link EntityZapEvent#getReplacementEntity()}
     */
    @NotNull
    @Deprecated(since = "1.13.2")
    public PigZombie getPigZombie() {
        return (PigZombie) super.getReplacementEntity();
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        super.setCancelled(cancel);
    }
}
