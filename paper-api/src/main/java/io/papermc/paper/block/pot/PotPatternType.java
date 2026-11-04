package io.papermc.paper.block.pot;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.data.DecoratedPotPatternRegistryEntry;
import org.bukkit.Keyed;

/**
 * A pot pattern on a decorated pot.
 */
public interface PotPatternType extends RegistryElement.Buildable<PotPatternType, DecoratedPotPatternRegistryEntry, DecoratedPotPatternRegistryEntry.Builder>, Keyed {
}
