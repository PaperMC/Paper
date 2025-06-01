package org.bukkit.entity;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Translatable;
import org.bukkit.World;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum EntityType implements Keyed, Translatable, net.kyori.adventure.translation.Translatable, io.papermc.paper.world.flag.FeatureDependant { // Paper - translatable

    // Start generate - EntityType
    // @GeneratedFrom 1.21.6-pre1
    ACACIA_BOAT("acacia_boat", AcaciaBoat.class, -1),
    ACACIA_CHEST_BOAT("acacia_chest_boat", AcaciaChestBoat.class, -1),
    ALLAY("allay", Allay.class, -1),
    AREA_EFFECT_CLOUD("area_effect_cloud", AreaEffectCloud.class, 3),
    ARMADILLO("armadillo", Armadillo.class, -1),
    ARMOR_STAND("armor_stand", ArmorStand.class, 30),
    ARROW("arrow", Arrow.class, 10),
    AXOLOTL("axolotl", Axolotl.class, -1),
    BAMBOO_CHEST_RAFT("bamboo_chest_raft", BambooChestRaft.class, -1),
    BAMBOO_RAFT("bamboo_raft", BambooRaft.class, -1),
    BAT("bat", Bat.class, 65),
    BEE("bee", Bee.class, -1),
    BIRCH_BOAT("birch_boat", BirchBoat.class, -1),
    BIRCH_CHEST_BOAT("birch_chest_boat", BirchChestBoat.class, -1),
    BLAZE("blaze", Blaze.class, 61),
    BLOCK_DISPLAY("block_display", BlockDisplay.class, -1),
    BOGGED("bogged", Bogged.class, -1),
    BREEZE("breeze", Breeze.class, -1),
    BREEZE_WIND_CHARGE("breeze_wind_charge", BreezeWindCharge.class, -1),
    CAMEL("camel", Camel.class, -1),
    CAT("cat", Cat.class, -1),
    CAVE_SPIDER("cave_spider", CaveSpider.class, 59),
    CHERRY_BOAT("cherry_boat", CherryBoat.class, -1),
    CHERRY_CHEST_BOAT("cherry_chest_boat", CherryChestBoat.class, -1),
    CHEST_MINECART("chest_minecart", StorageMinecart.class, 43),
    CHICKEN("chicken", Chicken.class, 93),
    COD("cod", Cod.class, -1),
    COMMAND_BLOCK_MINECART("command_block_minecart", CommandMinecart.class, 40),
    COW("cow", Cow.class, 92),
    CREAKING("creaking", Creaking.class, -1),
    CREEPER("creeper", Creeper.class, 50),
    DARK_OAK_BOAT("dark_oak_boat", DarkOakBoat.class, -1),
    DARK_OAK_CHEST_BOAT("dark_oak_chest_boat", DarkOakChestBoat.class, -1),
    DOLPHIN("dolphin", Dolphin.class, -1),
    DONKEY("donkey", Donkey.class, 31),
    DRAGON_FIREBALL("dragon_fireball", DragonFireball.class, 26),
    DROWNED("drowned", Drowned.class, -1),
    EGG("egg", Egg.class, 7),
    ELDER_GUARDIAN("elder_guardian", ElderGuardian.class, 4),
    END_CRYSTAL("end_crystal", EnderCrystal.class, 200),
    ENDER_DRAGON("ender_dragon", EnderDragon.class, 63),
    ENDER_PEARL("ender_pearl", EnderPearl.class, 14),
    ENDERMAN("enderman", Enderman.class, 58),
    ENDERMITE("endermite", Endermite.class, 67),
    EVOKER("evoker", Evoker.class, 34),
    EVOKER_FANGS("evoker_fangs", EvokerFangs.class, 33),
    EXPERIENCE_BOTTLE("experience_bottle", ThrownExpBottle.class, 17),
    EXPERIENCE_ORB("experience_orb", ExperienceOrb.class, 2),
    EYE_OF_ENDER("eye_of_ender", EnderSignal.class, 15),
    FALLING_BLOCK("falling_block", FallingBlock.class, 21),
    FIREBALL("fireball", Fireball.class, 12),
    FIREWORK_ROCKET("firework_rocket", Firework.class, 22),
    FISHING_BOBBER("fishing_bobber", FishHook.class, -1, false),
    FOX("fox", Fox.class, -1),
    FROG("frog", Frog.class, -1),
    FURNACE_MINECART("furnace_minecart", PoweredMinecart.class, 44),
    GHAST("ghast", Ghast.class, 56),
    GIANT("giant", Giant.class, 53),
    GLOW_ITEM_FRAME("glow_item_frame", GlowItemFrame.class, -1),
    GLOW_SQUID("glow_squid", GlowSquid.class, -1),
    GOAT("goat", Goat.class, -1),
    GUARDIAN("guardian", Guardian.class, 68),
    HAPPY_GHAST("happy_ghast", HappyGhast.class, -1),
    HOGLIN("hoglin", Hoglin.class, -1),
    HOPPER_MINECART("hopper_minecart", HopperMinecart.class, 46),
    HORSE("horse", Horse.class, 100),
    HUSK("husk", Husk.class, 23),
    ILLUSIONER("illusioner", Illusioner.class, 37),
    INTERACTION("interaction", Interaction.class, -1),
    IRON_GOLEM("iron_golem", IronGolem.class, 99),
    ITEM("item", Item.class, 1),
    ITEM_DISPLAY("item_display", ItemDisplay.class, -1),
    ITEM_FRAME("item_frame", ItemFrame.class, 18),
    JUNGLE_BOAT("jungle_boat", JungleBoat.class, -1),
    JUNGLE_CHEST_BOAT("jungle_chest_boat", JungleChestBoat.class, -1),
    LEASH_KNOT("leash_knot", LeashHitch.class, 8),
    LIGHTNING_BOLT("lightning_bolt", LightningStrike.class, -1),
    LINGERING_POTION("lingering_potion", LingeringPotion.class, -1),
    LLAMA("llama", Llama.class, 103),
    LLAMA_SPIT("llama_spit", LlamaSpit.class, 104),
    MAGMA_CUBE("magma_cube", MagmaCube.class, 62),
    MANGROVE_BOAT("mangrove_boat", MangroveBoat.class, -1),
    MANGROVE_CHEST_BOAT("mangrove_chest_boat", MangroveChestBoat.class, -1),
    MARKER("marker", Marker.class, -1),
    MINECART("minecart", Minecart.class, 42),
    MOOSHROOM("mooshroom", MushroomCow.class, 96),
    MULE("mule", Mule.class, 32),
    OAK_BOAT("oak_boat", OakBoat.class, -1),
    OAK_CHEST_BOAT("oak_chest_boat", OakChestBoat.class, -1),
    OCELOT("ocelot", Ocelot.class, 98),
    OMINOUS_ITEM_SPAWNER("ominous_item_spawner", OminousItemSpawner.class, -1),
    PAINTING("painting", Painting.class, 9),
    PALE_OAK_BOAT("pale_oak_boat", PaleOakBoat.class, -1),
    PALE_OAK_CHEST_BOAT("pale_oak_chest_boat", PaleOakChestBoat.class, -1),
    PANDA("panda", Panda.class, -1),
    PARROT("parrot", Parrot.class, 105),
    PHANTOM("phantom", Phantom.class, -1),
    PIG("pig", Pig.class, 90),
    PIGLIN("piglin", Piglin.class, -1),
    PIGLIN_BRUTE("piglin_brute", PiglinBrute.class, -1),
    PILLAGER("pillager", Pillager.class, -1),
    PLAYER("player", Player.class, -1, false),
    POLAR_BEAR("polar_bear", PolarBear.class, 102),
    PUFFERFISH("pufferfish", PufferFish.class, -1),
    RABBIT("rabbit", Rabbit.class, 101),
    RAVAGER("ravager", Ravager.class, -1),
    SALMON("salmon", Salmon.class, -1),
    SHEEP("sheep", Sheep.class, 91),
    SHULKER("shulker", Shulker.class, 69),
    SHULKER_BULLET("shulker_bullet", ShulkerBullet.class, 25),
    SILVERFISH("silverfish", Silverfish.class, 60),
    SKELETON("skeleton", Skeleton.class, 51),
    SKELETON_HORSE("skeleton_horse", SkeletonHorse.class, 28),
    SLIME("slime", Slime.class, 55),
    SMALL_FIREBALL("small_fireball", SmallFireball.class, 13),
    SNIFFER("sniffer", Sniffer.class, -1),
    SNOW_GOLEM("snow_golem", Snowman.class, 97),
    SNOWBALL("snowball", Snowball.class, 11),
    SPAWNER_MINECART("spawner_minecart", SpawnerMinecart.class, 47),
    SPECTRAL_ARROW("spectral_arrow", SpectralArrow.class, 24),
    SPIDER("spider", Spider.class, 52),
    SPLASH_POTION("splash_potion", SplashPotion.class, 16),
    SPRUCE_BOAT("spruce_boat", SpruceBoat.class, -1),
    SPRUCE_CHEST_BOAT("spruce_chest_boat", SpruceChestBoat.class, -1),
    SQUID("squid", Squid.class, 94),
    STRAY("stray", Stray.class, 6),
    STRIDER("strider", Strider.class, -1),
    TADPOLE("tadpole", Tadpole.class, -1),
    TEXT_DISPLAY("text_display", TextDisplay.class, -1),
    TNT("tnt", TNTPrimed.class, 20),
    TNT_MINECART("tnt_minecart", ExplosiveMinecart.class, 45),
    TRADER_LLAMA("trader_llama", TraderLlama.class, -1),
    TRIDENT("trident", Trident.class, -1),
    TROPICAL_FISH("tropical_fish", TropicalFish.class, -1),
    TURTLE("turtle", Turtle.class, -1),
    VEX("vex", Vex.class, 35),
    VILLAGER("villager", Villager.class, 120),
    VINDICATOR("vindicator", Vindicator.class, 36),
    WANDERING_TRADER("wandering_trader", WanderingTrader.class, -1),
    WARDEN("warden", Warden.class, -1),
    WIND_CHARGE("wind_charge", WindCharge.class, -1),
    WITCH("witch", Witch.class, 66),
    WITHER("wither", Wither.class, 64),
    WITHER_SKELETON("wither_skeleton", WitherSkeleton.class, 5),
    WITHER_SKULL("wither_skull", WitherSkull.class, 19),
    WOLF("wolf", Wolf.class, 95),
    ZOGLIN("zoglin", Zoglin.class, -1),
    ZOMBIE("zombie", Zombie.class, 54),
    ZOMBIE_HORSE("zombie_horse", ZombieHorse.class, 29),
    ZOMBIE_VILLAGER("zombie_villager", ZombieVillager.class, 27),
    ZOMBIFIED_PIGLIN("zombified_piglin", PigZombie.class, 57),
    // End generate - EntityType
    /**
     * An unknown entity without an Entity Class
     */
    UNKNOWN(null, null, -1, false);

    private final String name;
    private final Class<? extends Entity> clazz;
    private final short typeId;
    private final boolean independent, living;
    private final NamespacedKey key;

    private static final Map<String, EntityType> NAME_MAP = new HashMap<String, EntityType>();
    private static final Map<Short, EntityType> ID_MAP = new HashMap<Short, EntityType>();

    static {
        for (EntityType type : values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(Locale.ROOT), type);
            }
            if (type.typeId > 0) {
                ID_MAP.put(type.typeId, type);
            }
        }
    }

    private EntityType(/*@Nullable*/ String name, /*@Nullable*/ Class<? extends Entity> clazz, int typeId) {
        this(name, clazz, typeId, true);
    }

    private EntityType(/*@Nullable*/ String name, /*@Nullable*/ Class<? extends Entity> clazz, int typeId, boolean independent) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
        this.independent = independent;
        this.living = clazz != null && LivingEntity.class.isAssignableFrom(clazz);
        this.key = (name == null) ? null : NamespacedKey.minecraft(name);
    }

    /**
     * Gets the entity type name.
     *
     * @return the entity type's name
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @Nullable
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        Preconditions.checkArgument(key != null, "EntityType doesn't have key! Is it UNKNOWN?");

        return key;
    }

    @Nullable
    public Class<? extends Entity> getEntityClass() {
        return clazz;
    }

    /**
     * Gets the entity type id.
     *
     * @return the raw type id
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    public short getTypeId() {
        return typeId;
    }

    /**
     * Gets an entity type from its name.
     *
     * @param name the entity type's name
     * @return the matching entity type or null
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    @Contract("null -> null")
    @Nullable
    public static EntityType fromName(@Nullable String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets an entity from its id.
     *
     * @param id the raw type id
     * @return the matching entity type or null
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    @Nullable
    public static EntityType fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    /**
     * Some entities cannot be spawned using {@link
     * World#spawnEntity(Location, EntityType)} or {@link
     * World#spawn(Location, Class)}, usually because they require additional
     * information in order to spawn.
     *
     * @return False if the entity type cannot be spawned
     */
    public boolean isSpawnable() {
        return independent;
    }

    public boolean isAlive() {
        return living;
    }

    @Override
    @NotNull
    @Deprecated(forRemoval = true) // Paper
    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    // Paper start
    /**
     * @throws IllegalArgumentException if the entity does not have a translation key (is probably a custom entity)
     */
    @Override
    public @NotNull String translationKey() {
        Preconditions.checkArgument(this != UNKNOWN, "UNKNOWN entities do not have translation keys");
        return org.bukkit.Bukkit.getUnsafe().getTranslationKey(this);
    }

    /**
     * Checks if the entity has default attributes.
     *
     * @return true if it has default attributes
     */
    public boolean hasDefaultAttributes() {
        return org.bukkit.Bukkit.getUnsafe().hasDefaultEntityAttributes(this.key);
    }

    /**
     * Gets the default attributes for the entity.
     *
     * @return an unmodifiable instance of Attributable for reading default attributes.
     * @throws IllegalArgumentException if the entity does not exist of have default attributes (use {@link #hasDefaultAttributes()} first)
     */
    public @NotNull org.bukkit.attribute.Attributable getDefaultAttributes() {
        return org.bukkit.Bukkit.getUnsafe().getDefaultEntityAttributes(this.key);
    }
    // Paper end

    /**
     * Gets if this EntityType is enabled by feature in a world.
     *
     * @param world the world to check
     * @return true if this EntityType can be used to spawn an Entity for this World.
     * @deprecated use {@link io.papermc.paper.world.flag.FeatureFlagSetHolder#isEnabled(io.papermc.paper.world.flag.FeatureDependant)}
     */
    @Deprecated(forRemoval = true, since = "1.20")
    public boolean isEnabledByFeature(@NotNull World world) {
        return Bukkit.getDataPackManager().isEnabledByFeature(this, world);
    }
}
