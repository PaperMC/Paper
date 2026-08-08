package io.papermc.paper.registry.event;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.BannerPatternRegistryEntry;
import io.papermc.paper.registry.data.CatTypeRegistryEntry;
import io.papermc.paper.registry.data.ChickenVariantRegistryEntry;
import io.papermc.paper.registry.data.CowVariantRegistryEntry;
import io.papermc.paper.registry.data.DamageTypeRegistryEntry;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.data.FrogVariantRegistryEntry;
import io.papermc.paper.registry.data.GameEventRegistryEntry;
import io.papermc.paper.registry.data.InstrumentRegistryEntry;
import io.papermc.paper.registry.data.JukeboxSongRegistryEntry;
import io.papermc.paper.registry.data.PaintingVariantRegistryEntry;
import io.papermc.paper.registry.data.PigVariantRegistryEntry;
import io.papermc.paper.registry.data.SulfurCubeArchetypeRegistryEntry;
import io.papermc.paper.registry.data.TrimMaterialRegistryEntry;
import io.papermc.paper.registry.data.TrimPatternRegistryEntry;
import io.papermc.paper.registry.data.WolfVariantRegistryEntry;
import io.papermc.paper.registry.data.ZombieNautilusVariantRegistryEntry;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;
import org.bukkit.Art;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.MusicInstrument;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Pig;
import org.bukkit.entity.SulfurCube;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieNautilus;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import static io.papermc.paper.registry.event.RegistryEventProviderImpl.create;

/**
 * Holds methods to create registry event types for various registry-related events.
 */
public final class RegistryEvents {

    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<GameEvent, GameEventRegistryEntry.Builder> GAME_EVENT = create(RegistryKey.GAME_EVENT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<TrimMaterial, TrimMaterialRegistryEntry.Builder> TRIM_MATERIAL = create(RegistryKey.TRIM_MATERIAL);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<TrimPattern, TrimPatternRegistryEntry.Builder> TRIM_PATTERN = create(RegistryKey.TRIM_PATTERN);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<DamageType, DamageTypeRegistryEntry.Builder> DAMAGE_TYPE = create(RegistryKey.DAMAGE_TYPE);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Wolf.Variant, WolfVariantRegistryEntry.Builder> WOLF_VARIANT = create(RegistryKey.WOLF_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Enchantment, EnchantmentRegistryEntry.Builder> ENCHANTMENT = create(RegistryKey.ENCHANTMENT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<JukeboxSong, JukeboxSongRegistryEntry.Builder> JUKEBOX_SONG = create(RegistryKey.JUKEBOX_SONG);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<PatternType, BannerPatternRegistryEntry.Builder> BANNER_PATTERN = create(RegistryKey.BANNER_PATTERN);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Art, PaintingVariantRegistryEntry.Builder> PAINTING_VARIANT = create(RegistryKey.PAINTING_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<MusicInstrument, InstrumentRegistryEntry.Builder> INSTRUMENT = create(RegistryKey.INSTRUMENT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Cat.Type, CatTypeRegistryEntry.Builder> CAT_VARIANT = create(RegistryKey.CAT_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Frog.Variant, FrogVariantRegistryEntry.Builder> FROG_VARIANT = create(RegistryKey.FROG_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Chicken.Variant, ChickenVariantRegistryEntry.Builder> CHICKEN_VARIANT = create(RegistryKey.CHICKEN_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Cow.Variant, CowVariantRegistryEntry.Builder> COW_VARIANT = create(RegistryKey.COW_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Pig.Variant, PigVariantRegistryEntry.Builder> PIG_VARIANT = create(RegistryKey.PIG_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<ZombieNautilus.Variant, ZombieNautilusVariantRegistryEntry.Builder> ZOMBIE_NAUTILUS_VARIANT = create(RegistryKey.ZOMBIE_NAUTILUS_VARIANT);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<SulfurCube.Archetype, SulfurCubeArchetypeRegistryEntry.Builder> SULFUR_CUBE_ARCHETYPE = create(RegistryKey.SULFUR_CUBE_ARCHETYPE);
    /**
     * @deprecated use {@link #entryAdd(RegistryKey)} and {@link #compose(RegistryKey)}
     */
    @Deprecated(forRemoval = true, since = "26.3")
    public static final RegistryEventProvider<Dialog, DialogRegistryEntry.Builder> DIALOG = create(RegistryKey.DIALOG);

    /**
     * Gets the event type for {@link RegistryEntryAddEvent} which is fired just before
     * an object is added to a registry.
     * <p>
     * Can be used in {@link io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler)}
     * to register a handler for {@link RegistryEntryAddEvent}.
     *
     * @param registryKey the registry key
     * @param <API> the api type
     * @param <E> the registry entry type
     * @param <B> the registry entry builder type
     *
     * @return the registry entry add event type
     */
    public static <API extends RegistryElement.Buildable<API, E, B>, E, B extends RegistryBuilder<API>> RegistryEntryAddEventType<API, B> entryAdd(final RegistryKey<API> registryKey) {
        return RegistryEventTypeProvider.provider().registryEntryAdd(registryKey);
    }

    /**
     * Gets the event type for {@link RegistryComposeEvent} which is fired after
     * a registry is loaded of expected elements. It allows for the registration of new objects.
     * <p>
     * Can be used in {@link io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler)}
     * to register a handler for {@link RegistryComposeEvent}.
     *
     * @param registryKey the registry key
     * @param <API> the api type
     * @param <E> the registry entry type
     * @param <B> the registry entry builder type
     *
     * @return the registry compose event type
     */
    public static <API extends RegistryElement.Buildable<API, E, B>, E, B extends RegistryBuilder<API>> LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<API, B>> compose(final RegistryKey<API> registryKey) {
        return RegistryEventTypeProvider.provider().registryCompose(registryKey);
    }

    private RegistryEvents() {
    }
}
