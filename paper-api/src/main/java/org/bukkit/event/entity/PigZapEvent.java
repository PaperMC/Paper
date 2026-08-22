package org.bukkit.event.entity;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import org.bukkit.Warning;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;

/**
 * Stores data for pigs being zapped
 *
 * @deprecated use {@link EntityZapEvent}
 */
@Deprecated(since = "26.2")
@Warning(reason = "This event has become obsolete, the more generic EntityZapEvent should be used instead.")
public interface PigZapEvent extends EntityZapEvent {

    @Override
    Pig getEntity();

    /**
     * Gets the bolt which is striking the pig.
     *
     * @return lightning entity
     * @deprecated use {@link EntityZapEvent#getBolt()}
     */
    @Deprecated(since = "26.2")
    LightningStrike getLightning();

    /**
     * Gets the zombified piglin that will replace the pig, provided the event is
     * not cancelled first.
     *
     * @return resulting entity
     * @deprecated use {@link EntityZapEvent#getReplacementEntity()}
     */
    @Deprecated(since = "1.13.2")
    PigZombie getPigZombie();
}
