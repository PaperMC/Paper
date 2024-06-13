package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.data.GameEventRegistryEntry;
import io.papermc.paper.registry.data.PaintingVariantRegistryEntry;
import org.bukkit.Art;
import org.bukkit.GameEvent;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.registry.event.RegistryEventProviderImpl.create;

/**
 * Holds providers for {@link RegistryEntryAddEvent} and {@link RegistryFreezeEvent}
 * handlers for each applicable registry.
 */
@ApiStatus.Experimental
@NullMarked
public final class RegistryEvents {

    public static final RegistryEventProvider<GameEvent, GameEventRegistryEntry.Builder> GAME_EVENT = create(RegistryKey.GAME_EVENT);
    public static final RegistryEventProvider<Enchantment, EnchantmentRegistryEntry.Builder> ENCHANTMENT = create(RegistryKey.ENCHANTMENT);
    public static final RegistryEventProvider<Art, PaintingVariantRegistryEntry.Builder> PAINTING_VARIANT = create(RegistryKey.PAINTING_VARIANT);

    private RegistryEvents() {
    }
}
