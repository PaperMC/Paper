package io.papermc.paper.datapack;

import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a snapshot of a datapack that the server has found by
 * searching available sources. It may or may not be enabled and isn't
 * guaranteed to be available. This object won't be
 * updated as datapacks are updated.
 * @see DatapackRegistrar
 */
@NullMarked
@ApiStatus.NonExtendable
public interface DiscoveredDatapack {

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
     * Gets if this datapack is required.
     * <p>
     * A "required" datapack will always be enabled on server startup, even if previously disabled.
     *
     * @return true if the pack is required
     */
    boolean isRequired();

    /**
     * Gets the compatibility status of this pack.
     *
     * @return the compatibility of the pack
     */
    Datapack.Compatibility getCompatibility();

    /**
     * Gets the set of required features for this datapack.
     *
     * @return the set of required features
     */
    @Unmodifiable
    Set<FeatureFlag> getRequiredFeatures();

    /**
     * Gets the source for this datapack.
     *
     * @return the pack source
     */
    DatapackSource getSource();
}
