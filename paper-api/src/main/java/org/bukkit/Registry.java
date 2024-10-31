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
     */
    Registry<Art> ART = new SimpleRegistry<>(Art.class);
    /**
     * Attribute.
     *
     * @see Attribute
     */
    Registry<Attribute> ATTRIBUTE = Objects.requireNonNull(Bukkit.getRegistry(Attribute.class), "No registry present for Attribute. This is a bug.");
    /**
     * Server banner patterns.
     *
     * @see PatternType
     */
    Registry<PatternType> BANNER_PATTERN = Objects.requireNonNull(Bukkit.getRegistry(PatternType.class), "No registry present for Pattern Type. This is a bug.");
    /**
     * Server biomes.
     *
     * @see Biome
     */
    Registry<Biome> BIOME = Objects.requireNonNull(Bukkit.getRegistry(Biome.class), "No registry present for Biome. This is a bug.");
    /**
     * Server block types.
     *
     * @see BlockType
     * @apiNote BlockType is not ready for public usage yet
     */
    @ApiStatus.Internal
    Registry<BlockType> BLOCK = Objects.requireNonNull(Bukkit.getRegistry(BlockType.class), "No registry present for BlockType. This is a bug.");
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
    Registry<Cat.Type> CAT_VARIANT = Objects.requireNonNull(Bukkit.getRegistry(Cat.Type.class), "No registry present for Cat Type. This is a bug.");
    /**
     * Server enchantments.
     *
     * @see Enchantment
     */
    Registry<Enchantment> ENCHANTMENT = Objects.requireNonNull(Bukkit.getRegistry(Enchantment.class), "No registry present for Enchantment. This is a bug.");
    /**
     * Server entity types.
     *
     * @see EntityType
     */
    Registry<EntityType> ENTITY_TYPE = new SimpleRegistry<>(EntityType.class, (entity) -> entity != EntityType.UNKNOWN);
    /**
     * Server instruments.
     *
     * @see MusicInstrument
     */
    Registry<MusicInstrument> INSTRUMENT = Objects.requireNonNull(Bukkit.getRegistry(MusicInstrument.class), "No registry present for MusicInstrument. This is a bug.");
    /**
     * Server item types.
     *
     * @see ItemType
     * @apiNote ItemType is not ready for public usage yet
     */
    @ApiStatus.Internal
    Registry<ItemType> ITEM = Objects.requireNonNull(Bukkit.getRegistry(ItemType.class), "No registry present for ItemType. This is a bug.");
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
    Registry<MenuType> MENU = Objects.requireNonNull(Bukkit.getRegistry(MenuType.class), "No registry present for MenuType. This is a bug.");
    /**
     * Server mob effects.
     *
     * @see PotionEffectType
     */
    Registry<PotionEffectType> EFFECT = Objects.requireNonNull(Bukkit.getRegistry(PotionEffectType.class), "No registry present for PotionEffectType. This is a bug.");
    /**
     * Server particles.
     *
     * @see Particle
     */
    Registry<Particle> PARTICLE_TYPE = new SimpleRegistry<>(Particle.class, (par) -> par.register);
    /**
     * Server potions.
     *
     * @see PotionType
     */
    Registry<PotionType> POTION = new SimpleRegistry<>(PotionType.class);
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
     */
    Registry<Structure> STRUCTURE = Objects.requireNonNull(Bukkit.getRegistry(Structure.class), "No registry present for Structure. This is a bug.");
    /**
     * Server structure types.
     *
     * @see StructureType
     */
    Registry<StructureType> STRUCTURE_TYPE = Objects.requireNonNull(Bukkit.getRegistry(StructureType.class), "No registry present for StructureType. This is a bug.");
    /**
     * Sound keys.
     *
     * @see Sound
     */
    Registry<Sound> SOUNDS = new SimpleRegistry<>(Sound.class);
    /**
     * Trim materials.
     *
     * @see TrimMaterial
     */
    @ApiStatus.Experimental
    Registry<TrimMaterial> TRIM_MATERIAL = Objects.requireNonNull(Bukkit.getRegistry(TrimMaterial.class), "No registry present for TrimMaterial. This is a bug.");
    /**
     * Trim patterns.
     *
     * @see TrimPattern
     */
    @ApiStatus.Experimental
    Registry<TrimPattern> TRIM_PATTERN = Objects.requireNonNull(Bukkit.getRegistry(TrimPattern.class), "No registry present for TrimPattern. This is a bug.");
    /**
     * Damage types.
     *
     * @see DamageType
     */
    @ApiStatus.Experimental
    Registry<DamageType> DAMAGE_TYPE = Objects.requireNonNull(Bukkit.getRegistry(DamageType.class), "No registry present for DamageType. This is a bug.");
    /**
     * Jukebox songs.
     *
     * @see JukeboxSong
     */
    @ApiStatus.Experimental
    Registry<JukeboxSong> JUKEBOX_SONG = Objects.requireNonNull(Bukkit.getRegistry(JukeboxSong.class), "No registry present for JukeboxSong. This is a bug.");
    /**
     * Villager profession.
     *
     * @see Villager.Profession
     */
    Registry<Villager.Profession> VILLAGER_PROFESSION = Objects.requireNonNull(Bukkit.getRegistry(Villager.Profession.class), "No registry present for Villager Profession. This is a bug.");
    /**
     * Villager type.
     *
     * @see Villager.Type
     */
    Registry<Villager.Type> VILLAGER_TYPE = Objects.requireNonNull(Bukkit.getRegistry(Villager.Type.class), "No registry present for Villager Type. This is a bug.");
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
    Registry<Fluid> FLUID = Objects.requireNonNull(Bukkit.getRegistry(Fluid.class), "No registry present for Fluid. This is a bug.");
    /**
     * Frog variants.
     *
     * @see Frog.Variant
     */
    Registry<Frog.Variant> FROG_VARIANT = Objects.requireNonNull(Bukkit.getRegistry(Frog.Variant.class), "No registry present for Frog Variant. This is a bug.");
    /**
     * Wolf variants.
     *
     * @see Wolf.Variant
     */
    Registry<Wolf.Variant> WOLF_VARIANT = Objects.requireNonNull(Bukkit.getRegistry(Wolf.Variant.class), "No registry present for Wolf Variant. This is a bug.");
    /**
     * Map cursor types.
     *
     * @see MapCursor.Type
     */
    Registry<MapCursor.Type> MAP_DECORATION_TYPE = Objects.requireNonNull(Bukkit.getRegistry(MapCursor.Type.class), "No registry present for MapCursor Type. This is a bug.");
    /**
     * Game events.
     *
     * @see GameEvent
     */
    Registry<GameEvent> GAME_EVENT = Objects.requireNonNull(Bukkit.getRegistry(GameEvent.class), "No registry present for GameEvent. This is a bug.");
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
     */
    @Nullable
    default T match(@NotNull String input) {
        Preconditions.checkArgument(input != null, "input must not be null");

        String filtered = input.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        NamespacedKey namespacedKey = NamespacedKey.fromString(filtered);
        return (namespacedKey != null) ? get(namespacedKey) : null;
    }

    static final class SimpleRegistry<T extends Enum<T> & Keyed> implements Registry<T> {

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
