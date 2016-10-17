package org.bukkit.event.entity;

import java.util.Collections;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for pigs being zapped
 */
public class PigZapEvent extends com.destroystokyo.paper.event.entity.EntityZapEvent implements Cancellable { // Paper
    // private static final HandlerList handlers = new HandlerList(); // Paper - moved in the super class
    private boolean canceled;
    private final PigZombie pigzombie;
    private final LightningStrike bolt;

    public PigZapEvent(@NotNull final Pig pig, @NotNull final LightningStrike bolt, @NotNull final PigZombie pigzombie) {
        super(pig, bolt, pigzombie); // Paper
        this.bolt = bolt;
        this.pigzombie = pigzombie;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @NotNull
    @Override
    public Pig getEntity() {
        return (Pig) entity;
    }

    /**
     * Gets the bolt which is striking the pig.
     *
     * @return lightning entity
     */
    @NotNull
    public LightningStrike getLightning() {
        return bolt;
    }

    /**
     * Gets the zombie pig that will replace the pig, provided the event is
     * not cancelled first.
     *
     * @return resulting entity
     * @deprecated use {@link EntityTransformEvent#getTransformedEntity()}
     */
    @NotNull
    @Deprecated(since = "1.13.2")
    public PigZombie getPigZombie() {
        return pigzombie;
    }

    // Paper start
    /*
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
    */
    // Paper end
}
