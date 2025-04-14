package io.papermc.paper.datapack;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * This is a snapshot of a datapack on the server. It
 * won't be updated as datapacks are updated.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface Datapack extends DiscoveredDatapack {

    /**
     * Gets the enabled state of this pack.
     *
     * @return whether the pack is currently enabled
     */
    boolean isEnabled();

    /**
     * Changes the enabled state of this pack. Will
     * cause a reload of resources ({@code /minecraft:reload}) if
     * any change happens.
     *
     * @param enabled true to enable, false to disable
     * @apiNote This method may be deprecated in the future as setters on a "snapshot" type are undesirable.
     */
    void setEnabled(boolean enabled);

    /**
     * Computes the component vanilla Minecraft uses
     * to display this datapack. Includes the {@link #getSource()},
     * {@link #getDescription()}, {@link #getName()}, and the enabled state.
     *
     * @return a new component
     */
    @Contract(pure = true, value = "-> new")
    Component computeDisplayName();

    enum Compatibility {
        TOO_OLD,
        TOO_NEW,
        COMPATIBLE,
    }

    /**
     * Position of the pack in the load order.
     */
    enum Position {
        TOP, BOTTOM
    }
}
