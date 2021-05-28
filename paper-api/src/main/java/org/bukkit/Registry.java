package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a registry of Bukkit objects that may be retrieved by
 * {@link NamespacedKey}.
 *
 * @param <T> type of item in the registry
 */
public interface Registry<T extends Keyed> extends Iterable<T> {

    /**
     * Server advancements.
     *
     * @see Bukkit#getAdvancement(org.bukkit.NamespacedKey)
     * @see Bukkit#advancementIterator()
     */
    Registry<Advancement> ADVANCEMENT = new Registry<Advancement>() {

        @Nullable
        @Override
        public Advancement get(@NotNull NamespacedKey key) {
            return Bukkit.getAdvancement(key);
        }

        @NotNull
        @Override
        public Advancement getOrThrow(@NotNull NamespacedKey key) {
            Advancement advancement = get(key);

            Preconditions.checkArgument(advancement != null, "No Advancement registry entry found for key %s.", key);

            return advancement;
        }

        @NotNull
        @Override
        public Stream<Advancement> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        @NotNull
        @Override
        public Iterator<Advancement> iterator() {
            return Bukkit.advancementIterator();
        }
    };
    /**
     * Server art.
     *
     * @see Art
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#PAINTING_VARIANT}
     */
    @Deprecated(since = "1.21.3") // Paper
    Registry<Art> ART = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(Art.class), "No registry present for Art. This is a bug.");
    /**
     * Attribute.
     *
     * @see Attribute
     */
    Registry<Attribute> ATTRIBUTE = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.ATTRIBUTE); // Paper
    /**
     * Server banner patterns.
     *
     * @see PatternType
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#BANNER_PATTERN}
     */
    @Deprecated(since = "1.21") // Paper
    Registry<PatternType> BANNER_PATTERN = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(PatternType.class), "No registry present for PatternType. This is a bug."); // Paper
    /**
     * Server biomes.
     *
     * @see Biome
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#BIOME}
     */
    @Deprecated(since = "1.21.3") // Paper
    Registry<Biome> BIOME = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(Biome.class), "No registry present for Biome. This is a bug.");
    /**
     * Server block types.
     *
     * @see BlockType
     * @apiNote BlockType is not ready for public usage yet
     */
    @ApiStatus.Internal
    Registry<BlockType> BLOCK = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.BLOCK); // Paper
    /**
     * Custom boss bars.
     *
     * @see Bukkit#getBossBar(org.bukkit.NamespacedKey)
     * @see Bukkit#getBossBars()
     */
    Registry<KeyedBossBar> BOSS_BARS = new Registry<KeyedBossBar>() {

        @Nullable
        @Override
        public KeyedBossBar get(@NotNull NamespacedKey key) {
            return Bukkit.getBossBar(key);
        }

        @NotNull
        @Override
        public KeyedBossBar getOrThrow(@NotNull NamespacedKey key) {
            KeyedBossBar keyedBossBar = get(key);

            Preconditions.checkArgument(keyedBossBar != null, "No KeyedBossBar registry entry found for key %s.", key);

            return keyedBossBar;
        }

        @NotNull
        @Override
        public Stream<KeyedBossBar> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        @NotNull
        @Override
        public Iterator<KeyedBossBar> iterator() {
            return Bukkit.getBossBars();
        }
    };
    /**
     * Server cat types.
     *
     * @see Cat.Type
     */
    Registry<Cat.Type> CAT_VARIANT = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.CAT_VARIANT); // Paper
    /**
     * Server enchantments.
     *
     * @see Enchantment
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#ENCHANTMENT}
     */
    @Deprecated(since = "1.21")
    Registry<Enchantment> ENCHANTMENT = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(Enchantment.class), "No registry present for Enchantment. This is a bug."); // Paper
    /**
     * Server entity types.
     *
     * @see EntityType
     */
    Registry<EntityType> ENTITY_TYPE = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.ENTITY_TYPE); // Paper
    /**
     * Server instruments.
     *
     * @see MusicInstrument
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#INSTRUMENT}
     */
    @Deprecated(since = "1.21.2")
    Registry<MusicInstrument> INSTRUMENT = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(MusicInstrument.class), "No registry present for Instruments. This is a bug."); // Paper
    /**
     * Server item types.
     *
     * @see ItemType
     * @apiNote ItemType is not ready for public usage yet
     */
    @ApiStatus.Internal
    Registry<ItemType> ITEM = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.ITEM); // Paper
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
    Registry<MenuType> MENU = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.MENU); // Paper
    /**
     * Server mob effects.
     *
     * @see PotionEffectType
     */
    Registry<PotionEffectType> EFFECT = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.MOB_EFFECT); // Paper
    /**
     * Server particles.
     *
     * @see Particle
     */
    Registry<Particle> PARTICLE_TYPE = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.PARTICLE_TYPE); // Paper
    /**
     * Server potions.
     *
     * @see PotionType
     */
    Registry<PotionType> POTION = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.POTION); // Paper
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
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#STRUCTURE}
     */
    @Deprecated(since = "1.20.6") // Paper
    Registry<Structure> STRUCTURE = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(Structure.class), "No registry present for Structure. This is a bug."); // Paper
    /**
     * Server structure types.
     *
     * @see StructureType
     */
    Registry<StructureType> STRUCTURE_TYPE = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.STRUCTURE_TYPE), "No registry present for StructureType. This is a bug."); // Paper
    /**
     * Sound keys.
     *
     * @see Sound
     */
    Registry<Sound> SOUNDS = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.SOUND_EVENT); // Paper
    /**
     * Trim materials.
     *
     * @see TrimMaterial
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}
     */
    @Deprecated(since = "1.20.6") // Paper
    Registry<TrimMaterial> TRIM_MATERIAL = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(TrimMaterial.class), "No registry present for TrimMaterial. This is a bug."); // Paper
    /**
     * Trim patterns.
     *
     * @see TrimPattern
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#TRIM_PATTERN}
     */
    @Deprecated(since = "1.20.6")
    Registry<TrimPattern> TRIM_PATTERN = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(TrimPattern.class), "No registry present for TrimPattern. This is a bug."); // Paper
    /**
     * Damage types.
     *
     * @see DamageType
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#DAMAGE_TYPE}
     */
    @Deprecated(since = "1.20.6")
    Registry<DamageType> DAMAGE_TYPE = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(DamageType.class), "No registry present for DamageType. This is a bug."); // Paper
    /**
     * Jukebox songs.
     *
     * @see JukeboxSong
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#JUKEBOX_SONG}
     */
    @ApiStatus.Experimental
    @Deprecated(since = "1.21")
    Registry<JukeboxSong> JUKEBOX_SONG = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(JukeboxSong.class), "No registry present for JukeboxSong. This is a bug."); // Paper
    /**
     * Villager profession.
     *
     * @see Villager.Profession
     */
    Registry<Villager.Profession> VILLAGER_PROFESSION = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.VILLAGER_PROFESSION); // Paper
    /**
     * Villager type.
     *
     * @see Villager.Type
     */
    Registry<Villager.Type> VILLAGER_TYPE = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.VILLAGER_TYPE); // Paper
    /**
     * Memory Keys.
     *
     * @see MemoryKey
     */
    Registry<MemoryKey> MEMORY_MODULE_TYPE = new Registry<MemoryKey>() {

        @NotNull
        @Override
        public Iterator iterator() {
            return MemoryKey.values().iterator();
        }

        @Nullable
        @Override
        public MemoryKey get(@NotNull NamespacedKey key) {
            return MemoryKey.getByKey(key);
        }

        @NotNull
        @Override
        public MemoryKey getOrThrow(@NotNull NamespacedKey key) {
            MemoryKey memoryKey = get(key);

            Preconditions.checkArgument(memoryKey != null, "No MemoryKey registry entry found for key %s.", key);

            return memoryKey;
        }

        @NotNull
        @Override
        public Stream<MemoryKey> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    };
    /**
     * Server fluids.
     *
     * @see Fluid
     */
    Registry<Fluid> FLUID = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.FLUID); // Paper
    /**
     * Frog variants.
     *
     * @see Frog.Variant
     */
    Registry<Frog.Variant> FROG_VARIANT = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.FROG_VARIANT); // Paper
    /**
     * Wolf variants.
     *
     * @see Wolf.Variant
     * @deprecated use {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} with {@link io.papermc.paper.registry.RegistryKey#WOLF_VARIANT}
     */
    @Deprecated(since = "1.20.6")
    Registry<Wolf.Variant> WOLF_VARIANT = Objects.requireNonNull(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(Wolf.Variant.class), "No registry present for Wolf$Variant. This is a bug."); // Paper
    /**
     * Map cursor types.
     *
     * @see MapCursor.Type
     */
    Registry<MapCursor.Type> MAP_DECORATION_TYPE = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.MAP_DECORATION_TYPE); // Paper
    /**
     * Game events.
     *
     * @see GameEvent
     */
    Registry<GameEvent> GAME_EVENT = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(io.papermc.paper.registry.RegistryKey.GAME_EVENT); // Paper

    // Paper start - potion effect type registry
    /**
     * Potion effect types.
     *
     * @see org.bukkit.potion.PotionEffectType
     */
    Registry<org.bukkit.potion.PotionEffectType> POTION_EFFECT_TYPE = EFFECT;
    // Paper end - potion effect type registry
    /**
     * Get the object by its key.
     *
     * @param key non-null key
     * @return item or null if does not exist
     */
    @Nullable
    T get(@NotNull NamespacedKey key);

    /**
     * Get the object by its key.
     *
     * If there is no object with the given key, an exception will be thrown.
     *
     * @param key to get the object from
     * @return object with the given key
     * @throws IllegalArgumentException if there is no object with the given key
     */
    @NotNull
    T getOrThrow(@NotNull NamespacedKey key);

    /**
     * Returns a new stream, which contains all registry items, which are registered to the registry.
     *
     * @return a stream of all registry items
     */
    @NotNull
    Stream<T> stream();

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
    @Nullable
    @Deprecated(forRemoval = true) // Paper
    default T match(@NotNull String input) {
        Preconditions.checkArgument(input != null, "input must not be null");

        String filtered = input.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        NamespacedKey namespacedKey = NamespacedKey.fromString(filtered);
        return (namespacedKey != null) ? get(namespacedKey) : null;
    }

    class SimpleRegistry<T extends Enum<T> & Keyed> implements Registry<T> { // Paper - remove final

        private final Class<T> type;
        private final Map<NamespacedKey, T> map;

        protected SimpleRegistry(@NotNull Class<T> type) {
            this(type, Predicates.<T>alwaysTrue());
        }

        protected SimpleRegistry(@NotNull Class<T> type, @NotNull Predicate<T> predicate) {
            ImmutableMap.Builder<NamespacedKey, T> builder = ImmutableMap.builder();

            for (T entry : type.getEnumConstants()) {
                if (predicate.test(entry)) {
                    builder.put(entry.getKey(), entry);
                }
            }

            map = builder.build();
            this.type = type;
        }

        @Nullable
        @Override
        public T get(@NotNull NamespacedKey key) {
            return map.get(key);
        }

        @NotNull
        @Override
        public T getOrThrow(@NotNull NamespacedKey key) {
            T object = get(key);

            Preconditions.checkArgument(object != null, "No %s registry entry found for key %s.", type, key);

            return object;
        }

        @NotNull
        @Override
        public Stream<T> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return map.values().iterator();
        }

        @ApiStatus.Internal
        @Deprecated(since = "1.20.6", forRemoval = true)
        public Class<T> getType() {
            return this.type;
        }
    }
}
