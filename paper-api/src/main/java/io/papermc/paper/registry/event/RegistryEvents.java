package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.BannerPatternRegistryEntry;
import io.papermc.paper.registry.data.CatTypeRegistryEntry;
import io.papermc.paper.registry.data.ChickenVariantRegistryEntry;
import io.papermc.paper.registry.data.CowVariantRegistryEntry;
import io.papermc.paper.registry.data.DamageTypeRegistryEntry;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.data.FrogVariantRegistryEntry;
import io.papermc.paper.registry.data.GameEventRegistryEntry;
import io.papermc.paper.registry.data.PaintingVariantRegistryEntry;
import io.papermc.paper.registry.data.PigVariantRegistryEntry;
import io.papermc.paper.registry.data.WolfVariantRegistryEntry;
import org.bukkit.Art;
import org.bukkit.GameEvent;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Wolf;
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

    // Start generate - RegistryEvents
    // @GeneratedFrom 1.21.6-pre1
    public static final RegistryEventProvider<GameEvent, GameEventRegistryEntry.Builder> GAME_EVENT = create(RegistryKey.GAME_EVENT);
    public static final RegistryEventProvider<DamageType, DamageTypeRegistryEntry.Builder> DAMAGE_TYPE = create(RegistryKey.DAMAGE_TYPE);
    public static final RegistryEventProvider<Wolf.Variant, WolfVariantRegistryEntry.Builder> WOLF_VARIANT = create(RegistryKey.WOLF_VARIANT);
    public static final RegistryEventProvider<Enchantment, EnchantmentRegistryEntry.Builder> ENCHANTMENT = create(RegistryKey.ENCHANTMENT);
    public static final RegistryEventProvider<PatternType, BannerPatternRegistryEntry.Builder> BANNER_PATTERN = create(RegistryKey.BANNER_PATTERN);
    public static final RegistryEventProvider<Art, PaintingVariantRegistryEntry.Builder> PAINTING_VARIANT = create(RegistryKey.PAINTING_VARIANT);
    public static final RegistryEventProvider<Cat.Type, CatTypeRegistryEntry.Builder> CAT_VARIANT = create(RegistryKey.CAT_VARIANT);
    public static final RegistryEventProvider<Frog.Variant, FrogVariantRegistryEntry.Builder> FROG_VARIANT = create(RegistryKey.FROG_VARIANT);
    public static final RegistryEventProvider<Chicken.Variant, ChickenVariantRegistryEntry.Builder> CHICKEN_VARIANT = create(RegistryKey.CHICKEN_VARIANT);
    public static final RegistryEventProvider<Cow.Variant, CowVariantRegistryEntry.Builder> COW_VARIANT = create(RegistryKey.COW_VARIANT);
    public static final RegistryEventProvider<Pig.Variant, PigVariantRegistryEntry.Builder> PIG_VARIANT = create(RegistryKey.PIG_VARIANT);
    // End generate - RegistryEvents

    private RegistryEvents() {
    }
}
