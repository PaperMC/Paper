package org.bukkit.entity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

/// A classification of entities which may behave differently than others or be
/// affected uniquely by enchantments and potion effects among other things.
public enum EntityCategory {

    /// Any uncategorized entity. No additional effects are applied to these
    /// entities relating to a categorization.
    NONE,
    /// Undead creatures. These creatures:
    ///
    ///   - Are damaged by potions of healing.
    ///   - Are healed by potions of harming.
    ///   - Are immune to drowning and poison.
    ///   - Are subject to burning in daylight (though not all).
    ///   - Sink in water (except [Drowned], [`Phantoms`][Phantom]
    ///     and [`Withers`][Wither]).
    ///   - Take additional damage from [Enchantment#SMITE].
    ///   - Are ignored by [`Withers`][Wither].
    ///
    UNDEAD,
    /// Entities of the arthropod family. These creatures:
    ///
    ///   - Take additional damage and receive [PotionEffectType#SLOWNESS]
    ///     from [Enchantment#BANE_OF_ARTHROPODS].
    ///   - Are immune to [PotionEffectType#POISON] if they are spiders.
    ///
    ARTHROPOD,
    /// Entities that participate in raids. These creatures:
    ///
    ///   - Are immune to damage from [EvokerFangs].
    ///   - Are ignored by [`vindicators`][Vindicator] named "Johnny".
    ///   - Are hostile to [`villagers`][Villager],
    ///     [`wandering traders`][WanderingTrader], [`iron golems`][IronGolem]
    ///     and [`players`][Player].
    ///
    ILLAGER,
    /// Entities that reside primarily underwater (excluding [Drowned]).
    /// These creatures:
    ///
    ///   - Take additional damage from [Enchantment#IMPALING].
    ///   - Are immune to drowning (excluding [`dolphins`][Dolphin]).
    ///   - Take suffocation damage when out of water for extended periods of
    ///     time (excluding [`guardians`][Guardian] and [`turtles`][Turtle]).
    ///   - Are capable of swimming in water rather than floating or sinking.
    ///
    WATER;
}
