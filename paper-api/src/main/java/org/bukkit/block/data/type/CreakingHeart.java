package org.bukkit.block.data.type;

import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.NotNull;

/**
 * 'creaking_heart_state' is the state of the block depending on the time and its position.
 * <br>
 * 'natural' is whether this is a naturally generated block.
 */
public interface CreakingHeart extends Orientable {

    /**
     * Gets the value of the 'active' property.
     *
     * @return the 'active' value
     * @deprecated use {@link #getCreakingHeartState()}
     */
    @Deprecated(since = "1.21.5", forRemoval = true)
    default boolean isActive() {
        return this.getCreakingHeartState() != State.UPROOTED;
    }

    /**
     * Sets the value of the 'active' property.
     *
     * @param active the new 'active' value
     * @deprecated use {@link #setCreakingHeartState(State)}
     */
    @Deprecated(since = "1.21.5", forRemoval = true)
    default void setActive(boolean active) {
        this.setCreakingHeartState(active ? State.AWAKE : State.UPROOTED);
    }

    /**
     * Gets the value of the 'creaking_heart_state' property.
     *
     * @return the 'creaking_heart_state' value
     */
    @NotNull
    State getCreakingHeartState();

    /**
     * Sets the value of the 'creaking_heart_state' property.
     *
     * @param state the new 'creaking_heart_state' value
     */
    void setCreakingHeartState(@NotNull State state);

    /**
     * Gets the value of the 'natural' property.
     *
     * @return the 'natural' value
     */
    boolean isNatural();

    /**
     * Sets the value of the 'natural' property.
     *
     * @param natural the new 'natural' value
     */
    void setNatural(boolean natural);

    enum State {
        UPROOTED,
        DORMANT,
        AWAKE
    }
}
