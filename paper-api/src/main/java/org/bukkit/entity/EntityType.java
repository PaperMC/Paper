package org.bukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.world.flag.FeatureDependant;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.boat.AcaciaBoat;
import org.bukkit.entity.boat.AcaciaChestBoat;
import org.bukkit.entity.boat.BambooChestRaft;
import org.bukkit.entity.boat.BambooRaft;
import org.bukkit.entity.boat.BirchBoat;
import org.bukkit.entity.boat.BirchChestBoat;
import org.bukkit.entity.boat.CherryBoat;
import org.bukkit.entity.boat.CherryChestBoat;
import org.bukkit.entity.boat.DarkOakBoat;
import org.bukkit.entity.boat.DarkOakChestBoat;
import org.bukkit.entity.boat.JungleBoat;
import org.bukkit.entity.boat.JungleChestBoat;
import org.bukkit.entity.boat.MangroveBoat;
import org.bukkit.entity.boat.MangroveChestBoat;
import org.bukkit.entity.boat.OakBoat;
import org.bukkit.entity.boat.OakChestBoat;
import org.bukkit.entity.boat.PaleOakBoat;
import org.bukkit.entity.boat.PaleOakChestBoat;
import org.bukkit.entity.boat.SpruceBoat;
import org.bukkit.entity.boat.SpruceChestBoat;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface EntityType<E extends Entity> extends OldEnum<EntityType<E>>, Keyed, Translatable, net.kyori.adventure.translation.Translatable, FeatureDependant {

    // Start generate - EntityType
    EntityType<AcaciaBoat> ACACIA_BOAT = getType("acacia_boat");

    EntityType<AcaciaChestBoat> ACACIA_CHEST_BOAT = getType("acacia_chest_boat");

    EntityType<Allay> ALLAY = getType("allay");

    EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD = getType("area_effect_cloud");

    EntityType<Armadillo> ARMADILLO = getType("armadillo");

    EntityType<ArmorStand> ARMOR_STAND = getType("armor_stand");

    EntityType<Arrow> ARROW = getType("arrow");

    EntityType<Axolotl> AXOLOTL = getType("axolotl");

    EntityType<BambooChestRaft> BAMBOO_CHEST_RAFT = getType("bamboo_chest_raft");

    EntityType<BambooRaft> BAMBOO_RAFT = getType("bamboo_raft");

    EntityType<Bat> BAT = getType("bat");

    EntityType<Bee> BEE = getType("bee");

    EntityType<BirchBoat> BIRCH_BOAT = getType("birch_boat");

    EntityType<BirchChestBoat> BIRCH_CHEST_BOAT = getType("birch_chest_boat");

    EntityType<Blaze> BLAZE = getType("blaze");

    EntityType<BlockDisplay> BLOCK_DISPLAY = getType("block_display");

    EntityType<Bogged> BOGGED = getType("bogged");

    EntityType<Breeze> BREEZE = getType("breeze");

    EntityType<BreezeWindCharge> BREEZE_WIND_CHARGE = getType("breeze_wind_charge");

    EntityType<Camel> CAMEL = getType("camel");

    EntityType<CamelHusk> CAMEL_HUSK = getType("camel_husk");

    EntityType<Cat> CAT = getType("cat");

    EntityType<CaveSpider> CAVE_SPIDER = getType("cave_spider");

    EntityType<CherryBoat> CHERRY_BOAT = getType("cherry_boat");

    EntityType<CherryChestBoat> CHERRY_CHEST_BOAT = getType("cherry_chest_boat");

    EntityType<StorageMinecart> CHEST_MINECART = getType("chest_minecart");

    EntityType<Chicken> CHICKEN = getType("chicken");

    EntityType<Cod> COD = getType("cod");

    EntityType<CommandMinecart> COMMAND_BLOCK_MINECART = getType("command_block_minecart");

    EntityType<CopperGolem> COPPER_GOLEM = getType("copper_golem");

    EntityType<Cow> COW = getType("cow");

    EntityType<Creaking> CREAKING = getType("creaking");

    EntityType<Creeper> CREEPER = getType("creeper");

    EntityType<DarkOakBoat> DARK_OAK_BOAT = getType("dark_oak_boat");

    EntityType<DarkOakChestBoat> DARK_OAK_CHEST_BOAT = getType("dark_oak_chest_boat");

    EntityType<Dolphin> DOLPHIN = getType("dolphin");

    EntityType<Donkey> DONKEY = getType("donkey");

    EntityType<DragonFireball> DRAGON_FIREBALL = getType("dragon_fireball");

    EntityType<Drowned> DROWNED = getType("drowned");

    EntityType<Egg> EGG = getType("egg");

    EntityType<ElderGuardian> ELDER_GUARDIAN = getType("elder_guardian");

    EntityType<EnderCrystal> END_CRYSTAL = getType("end_crystal");

    EntityType<EnderDragon> ENDER_DRAGON = getType("ender_dragon");

    EntityType<EnderPearl> ENDER_PEARL = getType("ender_pearl");

    EntityType<Enderman> ENDERMAN = getType("enderman");

    EntityType<Endermite> ENDERMITE = getType("endermite");

    EntityType<Evoker> EVOKER = getType("evoker");

    EntityType<EvokerFangs> EVOKER_FANGS = getType("evoker_fangs");

    EntityType<ThrownExpBottle> EXPERIENCE_BOTTLE = getType("experience_bottle");

    EntityType<ExperienceOrb> EXPERIENCE_ORB = getType("experience_orb");

    EntityType<EnderSignal> EYE_OF_ENDER = getType("eye_of_ender");

    EntityType<FallingBlock> FALLING_BLOCK = getType("falling_block");

    EntityType<Fireball> FIREBALL = getType("fireball");

    EntityType<Firework> FIREWORK_ROCKET = getType("firework_rocket");

    EntityType<FishHook> FISHING_BOBBER = getType("fishing_bobber");

    EntityType<Fox> FOX = getType("fox");

    EntityType<Frog> FROG = getType("frog");

    EntityType<PoweredMinecart> FURNACE_MINECART = getType("furnace_minecart");

    EntityType<Ghast> GHAST = getType("ghast");

    EntityType<Giant> GIANT = getType("giant");

    EntityType<GlowItemFrame> GLOW_ITEM_FRAME = getType("glow_item_frame");

    EntityType<GlowSquid> GLOW_SQUID = getType("glow_squid");

    EntityType<Goat> GOAT = getType("goat");

    EntityType<Guardian> GUARDIAN = getType("guardian");

    EntityType<HappyGhast> HAPPY_GHAST = getType("happy_ghast");

    EntityType<Hoglin> HOGLIN = getType("hoglin");

    EntityType<HopperMinecart> HOPPER_MINECART = getType("hopper_minecart");

    EntityType<Horse> HORSE = getType("horse");

    EntityType<Husk> HUSK = getType("husk");

    EntityType<Illusioner> ILLUSIONER = getType("illusioner");

    EntityType<Interaction> INTERACTION = getType("interaction");

    EntityType<IronGolem> IRON_GOLEM = getType("iron_golem");

    EntityType<Item> ITEM = getType("item");

    EntityType<ItemDisplay> ITEM_DISPLAY = getType("item_display");

    EntityType<ItemFrame> ITEM_FRAME = getType("item_frame");

    EntityType<JungleBoat> JUNGLE_BOAT = getType("jungle_boat");

    EntityType<JungleChestBoat> JUNGLE_CHEST_BOAT = getType("jungle_chest_boat");

    EntityType<LeashHitch> LEASH_KNOT = getType("leash_knot");

    EntityType<LightningStrike> LIGHTNING_BOLT = getType("lightning_bolt");

    EntityType<LingeringPotion> LINGERING_POTION = getType("lingering_potion");

    EntityType<Llama> LLAMA = getType("llama");

    EntityType<LlamaSpit> LLAMA_SPIT = getType("llama_spit");

    EntityType<MagmaCube> MAGMA_CUBE = getType("magma_cube");

    EntityType<MangroveBoat> MANGROVE_BOAT = getType("mangrove_boat");

    EntityType<MangroveChestBoat> MANGROVE_CHEST_BOAT = getType("mangrove_chest_boat");

    EntityType<Mannequin> MANNEQUIN = getType("mannequin");

    EntityType<Marker> MARKER = getType("marker");

    EntityType<RideableMinecart> MINECART = getType("minecart");

    EntityType<MushroomCow> MOOSHROOM = getType("mooshroom");

    EntityType<Mule> MULE = getType("mule");

    EntityType<Nautilus> NAUTILUS = getType("nautilus");

    EntityType<OakBoat> OAK_BOAT = getType("oak_boat");

    EntityType<OakChestBoat> OAK_CHEST_BOAT = getType("oak_chest_boat");

    EntityType<Ocelot> OCELOT = getType("ocelot");

    EntityType<OminousItemSpawner> OMINOUS_ITEM_SPAWNER = getType("ominous_item_spawner");

    EntityType<Painting> PAINTING = getType("painting");

    EntityType<PaleOakBoat> PALE_OAK_BOAT = getType("pale_oak_boat");

    EntityType<PaleOakChestBoat> PALE_OAK_CHEST_BOAT = getType("pale_oak_chest_boat");

    EntityType<Panda> PANDA = getType("panda");

    EntityType<Parched> PARCHED = getType("parched");

    EntityType<Parrot> PARROT = getType("parrot");

    EntityType<Phantom> PHANTOM = getType("phantom");

    EntityType<Pig> PIG = getType("pig");

    EntityType<Piglin> PIGLIN = getType("piglin");

    EntityType<PiglinBrute> PIGLIN_BRUTE = getType("piglin_brute");

    EntityType<Pillager> PILLAGER = getType("pillager");

    EntityType<Player> PLAYER = getType("player");

    EntityType<PolarBear> POLAR_BEAR = getType("polar_bear");

    EntityType<PufferFish> PUFFERFISH = getType("pufferfish");

    EntityType<Rabbit> RABBIT = getType("rabbit");

    EntityType<Ravager> RAVAGER = getType("ravager");

    EntityType<Salmon> SALMON = getType("salmon");

    EntityType<Sheep> SHEEP = getType("sheep");

    EntityType<Shulker> SHULKER = getType("shulker");

    EntityType<ShulkerBullet> SHULKER_BULLET = getType("shulker_bullet");

    EntityType<Silverfish> SILVERFISH = getType("silverfish");

    EntityType<Skeleton> SKELETON = getType("skeleton");

    EntityType<SkeletonHorse> SKELETON_HORSE = getType("skeleton_horse");

    EntityType<Slime> SLIME = getType("slime");

    EntityType<SmallFireball> SMALL_FIREBALL = getType("small_fireball");

    EntityType<Sniffer> SNIFFER = getType("sniffer");

    EntityType<Snowman> SNOW_GOLEM = getType("snow_golem");

    EntityType<Snowball> SNOWBALL = getType("snowball");

    EntityType<SpawnerMinecart> SPAWNER_MINECART = getType("spawner_minecart");

    EntityType<SpectralArrow> SPECTRAL_ARROW = getType("spectral_arrow");

    EntityType<Spider> SPIDER = getType("spider");

    EntityType<SplashPotion> SPLASH_POTION = getType("splash_potion");

    EntityType<SpruceBoat> SPRUCE_BOAT = getType("spruce_boat");

    EntityType<SpruceChestBoat> SPRUCE_CHEST_BOAT = getType("spruce_chest_boat");

    EntityType<Squid> SQUID = getType("squid");

    EntityType<Stray> STRAY = getType("stray");

    EntityType<Strider> STRIDER = getType("strider");

    EntityType<Tadpole> TADPOLE = getType("tadpole");

    EntityType<TextDisplay> TEXT_DISPLAY = getType("text_display");

    EntityType<TNTPrimed> TNT = getType("tnt");

    EntityType<ExplosiveMinecart> TNT_MINECART = getType("tnt_minecart");

    EntityType<TraderLlama> TRADER_LLAMA = getType("trader_llama");

    EntityType<Trident> TRIDENT = getType("trident");

    EntityType<TropicalFish> TROPICAL_FISH = getType("tropical_fish");

    EntityType<Turtle> TURTLE = getType("turtle");

    EntityType<Vex> VEX = getType("vex");

    EntityType<Villager> VILLAGER = getType("villager");

    EntityType<Vindicator> VINDICATOR = getType("vindicator");

    EntityType<WanderingTrader> WANDERING_TRADER = getType("wandering_trader");

    EntityType<Warden> WARDEN = getType("warden");

    EntityType<WindCharge> WIND_CHARGE = getType("wind_charge");

    EntityType<Witch> WITCH = getType("witch");

    EntityType<Wither> WITHER = getType("wither");

    EntityType<WitherSkeleton> WITHER_SKELETON = getType("wither_skeleton");

    EntityType<WitherSkull> WITHER_SKULL = getType("wither_skull");

    EntityType<Wolf> WOLF = getType("wolf");

    EntityType<Zoglin> ZOGLIN = getType("zoglin");

    EntityType<Zombie> ZOMBIE = getType("zombie");

    EntityType<ZombieHorse> ZOMBIE_HORSE = getType("zombie_horse");

    EntityType<ZombieNautilus> ZOMBIE_NAUTILUS = getType("zombie_nautilus");

    EntityType<ZombieVillager> ZOMBIE_VILLAGER = getType("zombie_villager");

    EntityType<PigZombie> ZOMBIFIED_PIGLIN = getType("zombified_piglin");
    // End generate - EntityType

    /**
     * An unknown entity without an Entity Class
     */
    @Deprecated(since = "1.21.11", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    EntityType<?> UNKNOWN = InternalAPIBridge.get().constructLegacyUnknownEntityType();

    @SuppressWarnings("unchecked")
    private static <E extends Entity> EntityType<E> getType(final @KeyPattern.Value String key) {
        return (EntityType<E>) Registry.ENTITY_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    /**
     * Gets the entity type name.
     *
     * @return the entity type's name
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @Nullable String getName();

    @Nullable Class<? extends Entity> getEntityClass();

    /**
     * Gets the entity type id.
     *
     * @return the raw type id
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    short getTypeId();

    /**
     * Gets an entity type from its name.
     *
     * @param name the entity type's name
     * @return the matching entity type or null
     * @apiNote Internal Use Only
     */
    @ApiStatus.Internal
    @Contract("null -> null")
    static @Nullable EntityType<?> fromName(@Nullable String name) {
        if (name == null) {
            return null;
        }
        return Registry.ENTITY_TYPE.get(Key.key(Key.MINECRAFT_NAMESPACE, name.toLowerCase(Locale.ROOT)));
    }

    /**
     * Gets an entity from its id.
     *
     * @param id the raw type id
     * @return the matching entity type or null
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    static @Nullable EntityType<?> fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return null;
        //return ID_MAP.get((short) id);
    }

    /**
     * Some entities cannot be spawned using {@link
     * World#spawnEntity(Location, EntityType)} or {@link
     * World#spawn(Location, Class)}, usually because they require additional
     * information in order to spawn.
     *
     * @return False if the entity type cannot be spawned
     */
    boolean isSpawnable();

    boolean isAlive();

    /**
     * @throws IllegalArgumentException if the entity does not have a translation key (is probably a custom entity)
     */
    @Override
    default String getTranslationKey() {
        return this.translationKey();
    }

    /**
     * @throws IllegalArgumentException if the entity does not have a translation key (is probably a custom entity)
     */
    @Override
    String translationKey();

    /**
     * Checks if the entity has default attributes.
     *
     * @return true if it has default attributes
     */
    boolean hasDefaultAttributes();

    /**
     * Gets the default attributes for the entity.
     *
     * @return an unmodifiable instance of Attributable for reading default attributes.
     * @throws IllegalArgumentException if the entity does not exist of have default attributes (use {@link #hasDefaultAttributes()} first)
     */
    Attributable getDefaultAttributes();

    /**
     * @param name of the entity type.
     * @return the entity type with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @Deprecated(since = "1.21.11", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static EntityType<?> valueOf(String name) {
        if ("UNKNOWN".equals(name)) {
            return EntityType.UNKNOWN;
        }

        NamespacedKey key = NamespacedKey.fromString(name.toLowerCase(Locale.ROOT));
        EntityType<?> entityType = key == null ? null : Bukkit.getUnsafe().get(RegistryKey.ENTITY_TYPE, key);
        Preconditions.checkArgument(entityType != null, "No entity type found with the name %s", name);
        return entityType;
    }

    /**
     * @return an array of all known entity types.
     * @deprecated use {@link Registry#stream()}.
     */
    @Deprecated(since = "1.21.11", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static EntityType<?>[] values() {
        return Registry.ENTITY_TYPE.stream().toArray(EntityType[]::new);
    }
}
