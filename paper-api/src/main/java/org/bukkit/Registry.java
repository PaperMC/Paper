package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.kyori.adventure.key.Key;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.loot.LootTables;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a registry of Bukkit objects that may be retrieved by
 * {@link NamespacedKey}.
 *
 * @param <T> type of item in the registry
 */
@NullMarked
public interface Registry<T extends Keyed> extends Iterable<T> {

    private static <A extends Keyed> Registry<A> registryFor(final RegistryKey<A> registryKey) {
        return RegistryAccess.registryAccess().getRegistry(registryKey);
    }

    @SuppressWarnings("removal")
    @Deprecated(forRemoval = true, since = "1.21.4")
    private static <A extends Keyed> Registry<A> legacyRegistryFor(final Class<A> clazz) {
        return Objects.requireNonNull(RegistryAccess.registryAccess().getRegistry(clazz), () -> "No registry present for " + clazz.getSimpleName() + ". This is a bug.");
    }

    /**
     * Server advancements.
     *
     * @see Bukkit#getAdvancement(NamespacedKey)
     * @see Bukkit#advancementIterator()
     * @deprecated use {@link Bukkit#getAdvancement(NamespacedKey)} and {@link Bukkit#advancementIterator()}
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    Registry<Advancement> ADVANCEMENT = new NotARegistry<>() {

        @Override
        public @Nullable Advancement get(final NamespacedKey key) {
            return Bukkit.getAdvancement(key);
        }

        @Override
        public Iterator<Advancement> iterator() {
            return Bukkit.advancementIterator();
        }
    };
    /**
     * Server art.
     *
     * @see Art
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#PAINTING_VARIANT}
     */
    @Deprecated(since = "1.21.3") // Paper
    Registry<Art> ART = legacyRegistryFor(Art.class);
    /**
     * Attribute.
     *
     * @see Attribute
     */
    Registry<Attribute> ATTRIBUTE = registryFor(RegistryKey.ATTRIBUTE);
    /**
     * Server banner patterns.
     *
     * @see PatternType
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#BANNER_PATTERN}
     */
    @Deprecated(since = "1.21") // Paper
    Registry<PatternType> BANNER_PATTERN = legacyRegistryFor(PatternType.class);
    /**
     * Server biomes.
     *
     * @see Biome
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#BIOME}
     */
    @Deprecated(since = "1.21.3") // Paper
    Registry<Biome> BIOME = legacyRegistryFor(Biome.class);
    /**
     * Server block types.
     *
     * @see BlockType
     */
    Registry<BlockType> BLOCK = registryFor(RegistryKey.BLOCK);
    /**
     * Custom boss bars.
     *
     * @see Bukkit#getBossBar(org.bukkit.NamespacedKey)
     * @see Bukkit#getBossBars()
     * @deprecated use {@link Bukkit#getBossBar(NamespacedKey)} and {@link Bukkit#getBossBars()}
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    Registry<KeyedBossBar> BOSS_BARS = new NotARegistry<>() {

        @Override
        public @Nullable KeyedBossBar get(final NamespacedKey key) {
            return Bukkit.getBossBar(key);
        }

        @Override
        public Iterator<KeyedBossBar> iterator() {
            return Bukkit.getBossBars();
        }
    };
    /**
     * Server cat types.
     *
     * @see Cat.Type
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#CAT_VARIANT}
     */
    @Deprecated(since = "1.21.5")
    Registry<Cat.Type> CAT_VARIANT = legacyRegistryFor(Cat.Type.class);
    /**
     * Server enchantments.
     *
     * @see Enchantment
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#ENCHANTMENT}
     */
    @Deprecated(since = "1.21")
    Registry<Enchantment> ENCHANTMENT = legacyRegistryFor(Enchantment.class);
    /**
     * Server entity types.
     *
     * @see EntityType
     */
    Registry<EntityType> ENTITY_TYPE = registryFor(RegistryKey.ENTITY_TYPE);
    /**
     * Server instruments.
     *
     * @see MusicInstrument
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#INSTRUMENT}
     */
    @Deprecated(since = "1.21.2")
    Registry<MusicInstrument> INSTRUMENT = legacyRegistryFor(MusicInstrument.class);
    /**
     * Server item types.
     *
     * @see ItemType
     */
    Registry<ItemType> ITEM = registryFor(RegistryKey.ITEM);
    /**
     * Default server loot tables.
     *
     * @see LootTables
     */
    Registry<LootTables> LOOT_TABLES = new SimpleRegistry<>(LootTables.class);
    /**
     * Server materials.
     *
     * @see Material
     */
    Registry<Material> MATERIAL = new SimpleRegistry<>(Material.class, (mat) -> !mat.isLegacy());
    /**
     * Server menus.
     *
     * @see MenuType
     */
    @ApiStatus.Experimental
    Registry<MenuType> MENU = registryFor(RegistryKey.MENU);
    /**
     * Server mob effects.
     *
     * @see PotionEffectType
     */
    Registry<PotionEffectType> MOB_EFFECT = registryFor(RegistryKey.MOB_EFFECT);
    /**
     * Server particles.
     *
     * @see Particle
     */
    Registry<Particle> PARTICLE_TYPE = registryFor(RegistryKey.PARTICLE_TYPE); // Paper
    /**
     * Server potions.
     *
     * @see PotionType
     */
    Registry<PotionType> POTION = registryFor(RegistryKey.POTION); // Paper
    /**
     * Server statistics.
     *
     * @see Statistic
     */
    Registry<Statistic> STATISTIC = new SimpleRegistry<>(Statistic.class);
    /**
     * Server structures.
     *
     * @see Structure
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#STRUCTURE}
     */
    @Deprecated(since = "1.20.6") // Paper
    Registry<Structure> STRUCTURE = legacyRegistryFor(Structure.class);
    /**
     * Server structure types.
     *
     * @see StructureType
     */
    Registry<StructureType> STRUCTURE_TYPE = registryFor(RegistryKey.STRUCTURE_TYPE);
    /**
     * Sound events.
     *
     * @see Sound
     */
    Registry<Sound> SOUND_EVENT = registryFor(RegistryKey.SOUND_EVENT);
    /**
     * Trim materials.
     *
     * @see TrimMaterial
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#TRIM_MATERIAL}
     */
    @Deprecated(since = "1.20.6") // Paper
    Registry<TrimMaterial> TRIM_MATERIAL = legacyRegistryFor(TrimMaterial.class);
    /**
     * Trim patterns.
     *
     * @see TrimPattern
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#TRIM_PATTERN}
     */
    @Deprecated(since = "1.20.6")
    Registry<TrimPattern> TRIM_PATTERN = legacyRegistryFor(TrimPattern.class);
    /**
     * Damage types.
     *
     * @see DamageType
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#DAMAGE_TYPE}
     */
    @Deprecated(since = "1.20.6")
    Registry<DamageType> DAMAGE_TYPE = legacyRegistryFor(DamageType.class);
    /**
     * Jukebox songs.
     *
     * @see JukeboxSong
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#JUKEBOX_SONG}
     */
    @Deprecated(since = "1.21")
    Registry<JukeboxSong> JUKEBOX_SONG = legacyRegistryFor(JukeboxSong.class);
    /**
     * Villager profession.
     *
     * @see Villager.Profession
     */
    Registry<Villager.Profession> VILLAGER_PROFESSION = registryFor(RegistryKey.VILLAGER_PROFESSION);
    /**
     * Villager type.
     *
     * @see Villager.Type
     */
    Registry<Villager.Type> VILLAGER_TYPE = registryFor(RegistryKey.VILLAGER_TYPE);
    /**
     * Memory Keys.
     *
     * @see MemoryKey
     */
    Registry<MemoryKey<?>> MEMORY_MODULE_TYPE = new NotARegistry<>() {

        @Override
        public Iterator<MemoryKey<?>> iterator() {
            return MemoryKey.values().iterator();
        }

        @Override
        public int size() {
            return MemoryKey.values().size();
        }

        @Override
        public @Nullable MemoryKey<?> get(final NamespacedKey key) {
            return MemoryKey.getByKey(key);
        }
    };
    /**
     * Server fluids.
     *
     * @see Fluid
     */
    Registry<Fluid> FLUID = registryFor(RegistryKey.FLUID);
    /**
     * Frog variants.
     *
     * @see Frog.Variant
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#FROG_VARIANT}
     */
    @Deprecated(since = "1.21.5")
    Registry<Frog.Variant> FROG_VARIANT = legacyRegistryFor(Frog.Variant.class);
    /**
     * Wolf variants.
     *
     * @see Wolf.Variant
     * @deprecated use {@link RegistryAccess#getRegistry(RegistryKey)} with {@link RegistryKey#WOLF_VARIANT}
     */
    @Deprecated(since = "1.20.6")
    Registry<Wolf.Variant> WOLF_VARIANT = legacyRegistryFor(Wolf.Variant.class);
    /**
     * Map cursor types.
     *
     * @see MapCursor.Type
     */
    Registry<MapCursor.Type> MAP_DECORATION_TYPE = registryFor(RegistryKey.MAP_DECORATION_TYPE);
    /**
     * Game events.
     *
     * @see GameEvent
     * @see io.papermc.paper.registry.event.RegistryEvents#GAME_EVENT
     */
    Registry<GameEvent> GAME_EVENT = registryFor(RegistryKey.GAME_EVENT);
    /**
     * Data component types.
     *
     * @see DataComponentType
     */
    Registry<DataComponentType> DATA_COMPONENT_TYPE = registryFor(RegistryKey.DATA_COMPONENT_TYPE); // Paper

    //<editor-fold desc="renames" defaultstate="collapsed">
    /**
     * @apiNote use {@link #MOB_EFFECT} instead
     * @hidden
     */
    @ApiStatus.Obsolete(since = "1.21.4")
    Registry<PotionEffectType> EFFECT = MOB_EFFECT;
    /**
     * @apiNote use {@link #MOB_EFFECT} instead
     * @hidden
     */
    @ApiStatus.Obsolete(since = "1.21.4")
    Registry<org.bukkit.potion.PotionEffectType> POTION_EFFECT_TYPE = EFFECT;
    /**
     * @apiNote use {@link #SOUND_EVENT}
     * @hidden
     */
    @ApiStatus.Obsolete(since = "1.21.4")
    Registry<Sound> SOUNDS = registryFor(RegistryKey.SOUND_EVENT);
    //</editor-fold>

    /**
     * Get the object by its key.
     *
     * @param key non-null key
     * @return item or null if it does not exist
     */
    @Nullable T get(NamespacedKey key);
    // Paper start

    /**
     * Get the object by its key.
     *
     * @param key non-null key
     * @return item or null if it does not exist
     */
    default @Nullable T get(final Key key) {
        return key instanceof final NamespacedKey nsKey ? this.get(nsKey) : this.get(new NamespacedKey(key.namespace(), key.value()));
    }

    /**
     * Get the object by its typed key.
     *
     * @param typedKey non-null typed key
     * @return item or null if it does not exist
     */
    default @Nullable T get(final TypedKey<T> typedKey) {
        Preconditions.checkArgument(typedKey != null, "typedKey cannot be null");
        return this.get(typedKey.key());
    }
    // Paper end

    // Paper start - improve Registry

    /**
     * Gets the object by its key or throws if it doesn't exist.
     *
     * @param key the key to get the object of in this registry
     * @return the object for the key
     * @throws NoSuchElementException if the key doesn't point to an object in the registry
     */
    default T getOrThrow(final net.kyori.adventure.key.Key key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        final T value = this.get(key);
        if (value == null) {
            throw new NoSuchElementException("No value for " + key + " in " + this);
        }
        return value;
    }

    /**
     * Gets the object by its key or throws if it doesn't exist.
     *
     * @param key the key to get the object of in this registry
     * @return the object for the key
     * @throws NoSuchElementException if the key doesn't point to an object in the registry
     */
    default T getOrThrow(final TypedKey<T> key) {
        final T value = this.get(key);
        if (value == null) {
            throw new NoSuchElementException("No value for " + key + " in " + this);
        }
        return value;
    }

    /**
     * Gets the key for this object or throws if it doesn't exist.
     * <p>
     * Some types can exist without being in a registry
     * and such will have no key associated with them. This
     * method throw an exception if it isn't in this registry.
     *
     * @param value the value to get the key of in this registry
     * @return the key for the value
     * @throws NoSuchElementException if the value doesn't exist in this registry
     * @see #getKey(Keyed)
     */
    default NamespacedKey getKeyOrThrow(final T value) {
        final NamespacedKey key = this.getKey(value);
        if (key == null) {
            throw new NoSuchElementException(value + " has no key in " + this);
        }
        return key;
    }

    /**
     * Get the key for this object.
     * <p>
     * Some types can exist without being in a registry
     * and such will have no key associated with them. This
     * method will return null.
     *
     * @param value the value to get the key of in this registry
     * @return the key for the value or null if not in the registry
     * @see #getKeyOrThrow(Keyed)
     */
    @Nullable NamespacedKey getKey(T value);
    // Paper end - improve Registry

    // Paper start - RegistrySet API
    /**
     * Checks if this registry has a tag with the given key.
     *
     * @param key the key to check for
     * @return true if this registry has a tag with the given key, false otherwise
     * @throws UnsupportedOperationException if this registry doesn't have or support tags
     * @see #getTag(TagKey)
     */
    boolean hasTag(TagKey<T> key);

    /**
     * Gets the named registry set (tag) for the given key.
     *
     * @param key the key to get the tag for
     * @return the tag for the key
     * @throws NoSuchElementException if no tag with the given key is found
     * @throws UnsupportedOperationException    if this registry doesn't have or support tags
     * @see #hasTag(TagKey)
     * @see #getTagValues(TagKey)
     */
    @ApiStatus.Experimental
    Tag<T> getTag(TagKey<T> key);

    /**
     * Gets the named registry set (tag) for the given key and resolves it with this registry.
     *
     * @param key the key to get the tag for
     * @return the resolved values
     * @throws NoSuchElementException        if no tag with the given key is found
     * @throws UnsupportedOperationException if this registry doesn't have or support tags
     * @see #getTag(TagKey)
     * @see Tag#resolve(Registry)
     */
    @ApiStatus.Experimental
    default Collection<T> getTagValues(final TagKey<T> key) {
        Tag<T> tag = this.getTag(key);
        return tag.resolve(this);
    }

    /**
     * Gets all the tags in this registry.
     *
     * @return a stream of all tags in this registry
     * @throws UnsupportedOperationException if this registry doesn't have or support tags
     */
    @ApiStatus.Experimental
    Collection<Tag<T>> getTags();
    // Paper end - RegistrySet API

    /**
     * Get the object by its key.
     * <p>
     * If there is no object with the given key, an exception will be thrown.
     *
     * @param key to get the object from
     * @return object with the given key
     * @throws NoSuchElementException if there is no object with the given key
     */
    default T getOrThrow(final NamespacedKey key) {
        return this.getOrThrow((Key) key);
    }

    /**
     * Returns a new stream, which contains all registry items, which are registered to the registry.
     *
     * @return a stream of all registry items
     */
    Stream<T> stream();

    /**
     * Returns a new stream, which contains all registry keys, which are registered to the registry.
     *
     * @return a stream of all registry keys
     */
    Stream<NamespacedKey> keyStream();

    /**
     * Attempts to match the registered object with the given key.
     * <p>
     * This will attempt to find a reasonable match based on the provided input
     * and may do so through unspecified means.
     *
     * @param input non-null input
     * @return registered object or null if does not exist
     * @deprecated this method's behavior is broken and not useful. If you want to get an object
     * based on its vanilla name, or a key, wrap it in a {@link NamespacedKey} object and use {@link #get(NamespacedKey)}
     */
    // Paper
    @Deprecated(forRemoval = true)
    default @Nullable T match(final String input) {
        Preconditions.checkArgument(input != null, "input must not be null");

        final String filtered = input.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        final NamespacedKey namespacedKey = NamespacedKey.fromString(filtered);
        return (namespacedKey != null) ? this.get(namespacedKey) : null;
    }

    /**
     * Gets the size of the registry.
     *
     * @return the size of the registry
     */
    int size();

    /**
     * @hidden
     */
    @ApiStatus.Internal
    class SimpleRegistry<T extends Enum<T> & Keyed> extends NotARegistry<T> { // Paper - remove final

        private final Class<T> type;
        private final Map<NamespacedKey, T> map;

        protected SimpleRegistry(final Class<T> type) {
            this(type, Predicates.alwaysTrue());
        }

        protected SimpleRegistry(final Class<T> type, final Predicate<T> predicate) {
            final ImmutableMap.Builder<NamespacedKey, T> builder = ImmutableMap.builder();

            for (final T entry : type.getEnumConstants()) {
                if (predicate.test(entry)) {
                    builder.put(entry.getKey(), entry);
                }
            }

            this.map = builder.build();
            this.type = type;
        }

        @Override
        public @Nullable T get(final NamespacedKey key) {
            return this.map.get(key);
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public Iterator<T> iterator() {
            return this.map.values().iterator();
        }

        @Override
        public Stream<NamespacedKey> keyStream() {
            return this.map.keySet().stream();
        }

        @ApiStatus.Internal
        @Deprecated(since = "1.20.6", forRemoval = true)
        public Class<T> getType() {
            return this.type;
        }
    }

    /**
     * @hidden
     */
    @ApiStatus.Internal
    abstract class NotARegistry<A extends Keyed> implements Registry<A> {

        @Override
        public Stream<A> stream() {
            return StreamSupport.stream(this.spliterator(), false);
        }

        @Override
        public Stream<NamespacedKey> keyStream() {
            return stream().map(this::getKey);
        }

        @Override
        public int size() {
            return Iterables.size(this);
        }

        @Override
        public NamespacedKey getKey(final A value) {
            return value.getKey();
        }

        @Override
        public boolean hasTag(final TagKey<A> key) {
            throw new UnsupportedOperationException("This is not a real registry and therefore cannot support tags");
        }

        @Override
        public Tag<A> getTag(final TagKey<A> key) {
            throw new UnsupportedOperationException("This is not a real registry and therefore cannot support tags");
        }

        @Override
        public Collection<Tag<A>> getTags() {
            throw new UnsupportedOperationException("This is not a real registry and therefore cannot support tags");
        }
    }
}
