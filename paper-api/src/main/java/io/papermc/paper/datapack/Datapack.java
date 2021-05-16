package io.papermc.paper.datapack;

import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * This is a snapshot of a datapack on the server. It
 * won't be updated as datapacks are updated.
 */
@NullMarked
public interface Datapack {

    /**
     * Gets the name/id of this datapack.
     *
     * @return the name of the pack
     */
    @Contract(pure = true)
    String getName();

    /**
     * Gets the title component of this datapack.
     *
     * @return the title
     */
    Component getTitle();

    /**
     * Gets the description component of this datapack.
     *
     * @return the description
     */
    Component getDescription();

    /**
     * Gets if this datapack is required to be enabled.
     *
     * @return true if the pack is required
     */
    boolean isRequired();

    /**
     * Gets the compatibility status of this pack.
     *
     * @return the compatibility of the pack
     */
    Compatibility getCompatibility();

    /**
     * Gets the set of required features for this datapack.
     *
     * @return the set of required features
     */
    @Unmodifiable Set<FeatureFlag> getRequiredFeatures();

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
     * Gets the source for this datapack.
     *
     * @return the pack source
     */
    DatapackSource getSource();

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
}
