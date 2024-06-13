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

public enum EntityType implements Keyed, Translatable {

    // These strings MUST match the strings in nms.EntityTypes and are case sensitive.
    /**
     * An item resting on the ground.
     * <p>
     * Spawn with {@link World#dropItem(Location, ItemStack)} or {@link
     * World#dropItemNaturally(Location, ItemStack)}
     */
    ITEM("item", Item.class, 1),
    /**
     * An experience orb.
     */
    EXPERIENCE_ORB("experience_orb", ExperienceOrb.class, 2),
    /**
     * @see AreaEffectCloud
     */
    AREA_EFFECT_CLOUD("area_effect_cloud", AreaEffectCloud.class, 3),
    /**
     * @see ElderGuardian
     */
    ELDER_GUARDIAN("elder_guardian", ElderGuardian.class, 4),
    /**
     * @see WitherSkeleton
     */
    WITHER_SKELETON("wither_skeleton", WitherSkeleton.class, 5),
    /**
     * @see Stray
     */
    STRAY("stray", Stray.class, 6),
    /**
     * A flying chicken egg.
     */
    EGG("egg", Egg.class, 7),
    /**
     * A leash attached to a fencepost.
     */
    LEASH_KNOT("leash_knot", LeashHitch.class, 8),
    /**
     * A painting on a wall.
     */
    PAINTING("painting", Painting.class, 9),
    /**
     * An arrow projectile; may get stuck in the ground.
     */
    ARROW("arrow", Arrow.class, 10),
    /**
     * A flying snowball.
     */
    SNOWBALL("snowball", Snowball.class, 11),
    /**
     * A flying large fireball, as thrown by a Ghast for example.
     */
    FIREBALL("fireball", LargeFireball.class, 12),
    /**
     * A flying small fireball, such as thrown by a Blaze or player.
     */
    SMALL_FIREBALL("small_fireball", SmallFireball.class, 13),
    /**
     * A flying ender pearl.
     */
    ENDER_PEARL("ender_pearl", EnderPearl.class, 14),
    /**
     * An ender eye signal.
     */
    EYE_OF_ENDER("eye_of_ender", EnderSignal.class, 15),
    /**
     * A flying splash potion.
     */
    POTION("potion", ThrownPotion.class, 16),
    /**
     * A flying experience bottle.
     */
    EXPERIENCE_BOTTLE("experience_bottle", ThrownExpBottle.class, 17),
    /**
     * An item frame on a wall.
     */
    ITEM_FRAME("item_frame", ItemFrame.class, 18),
    /**
     * A flying wither skull projectile.
     */
    WITHER_SKULL("wither_skull", WitherSkull.class, 19),
    /**
     * Primed TNT that is about to explode.
     */
    TNT("tnt", TNTPrimed.class, 20),
    /**
     * A block that is going to or is about to fall.
     */
    FALLING_BLOCK("falling_block", FallingBlock.class, 21),
    /**
     * Internal representation of a Firework once it has been launched.
     */
    FIREWORK_ROCKET("firework_rocket", Firework.class, 22),
    /**
     * @see Husk
     */
    HUSK("husk", Husk.class, 23),
    /**
     * Like {@link #ARROW} but causes the {@link PotionEffectType#GLOWING} effect on all team members.
     */
    SPECTRAL_ARROW("spectral_arrow", SpectralArrow.class, 24),
    /**
     * Bullet fired by {@link #SHULKER}.
     */
    SHULKER_BULLET("shulker_bullet", ShulkerBullet.class, 25),
    /**
     * Like {@link #FIREBALL} but with added effects.
     */
    DRAGON_FIREBALL("dragon_fireball", DragonFireball.class, 26),
    /**
     * @see ZombieVillager
     */
    ZOMBIE_VILLAGER("zombie_villager", ZombieVillager.class, 27),
    /**
     * @see SkeletonHorse
     */
    SKELETON_HORSE("skeleton_horse", SkeletonHorse.class, 28),
    /**
     * @see ZombieHorse
     */
    ZOMBIE_HORSE("zombie_horse", ZombieHorse.class, 29),
    /**
     * Mechanical entity with an inventory for placing weapons / armor into.
     */
    ARMOR_STAND("armor_stand", ArmorStand.class, 30),
    /**
     * @see Donkey
     */
    DONKEY("donkey", Donkey.class, 31),
    /**
     * @see Mule
     */
    MULE("mule", Mule.class, 32),
    /**
     * @see EvokerFangs
     */
    EVOKER_FANGS("evoker_fangs", EvokerFangs.class, 33),
    /**
     * @see Evoker
     */
    EVOKER("evoker", Evoker.class, 34),
    /**
     * @see Vex
     */
    VEX("vex", Vex.class, 35),
    /**
     * @see Vindicator
     */
    VINDICATOR("vindicator", Vindicator.class, 36),
    /**
     * @see Illusioner
     */
    ILLUSIONER("illusioner", Illusioner.class, 37),
    /**
     * @see CommandMinecart
     */
    COMMAND_BLOCK_MINECART("command_block_minecart", CommandMinecart.class, 40),
    /**
     * A placed boat.
     */
    BOAT("boat", Boat.class, 41),
    /**
     * @see RideableMinecart
     */
    MINECART("minecart", RideableMinecart.class, 42),
    /**
     * @see StorageMinecart
     */
    CHEST_MINECART("chest_minecart", StorageMinecart.class, 43),
    /**
     * @see PoweredMinecart
     */
    FURNACE_MINECART("furnace_minecart", PoweredMinecart.class, 44),
    /**
     * @see ExplosiveMinecart
     */
    TNT_MINECART("tnt_minecart", ExplosiveMinecart.class, 45),
    /**
     * @see HopperMinecart
     */
    HOPPER_MINECART("hopper_minecart", HopperMinecart.class, 46),
    /**
     * @see SpawnerMinecart
     */
    SPAWNER_MINECART("spawner_minecart", SpawnerMinecart.class, 47),
    CREEPER("creeper", Creeper.class, 50),
    SKELETON("skeleton", Skeleton.class, 51),
    SPIDER("spider", Spider.class, 52),
    GIANT("giant", Giant.class, 53),
    ZOMBIE("zombie", Zombie.class, 54),
    SLIME("slime", Slime.class, 55),
    GHAST("ghast", Ghast.class, 56),
    ZOMBIFIED_PIGLIN("zombified_piglin", PigZombie.class, 57),
    ENDERMAN("enderman", Enderman.class, 58),
    CAVE_SPIDER("cave_spider", CaveSpider.class, 59),
    SILVERFISH("silverfish", Silverfish.class, 60),
    BLAZE("blaze", Blaze.class, 61),
    MAGMA_CUBE("magma_cube", MagmaCube.class, 62),
    ENDER_DRAGON("ender_dragon", EnderDragon.class, 63),
    WITHER("wither", Wither.class, 64),
    BAT("bat", Bat.class, 65),
    WITCH("witch", Witch.class, 66),
    ENDERMITE("endermite", Endermite.class, 67),
    GUARDIAN("guardian", Guardian.class, 68),
    SHULKER("shulker", Shulker.class, 69),
    PIG("pig", Pig.class, 90),
    SHEEP("sheep", Sheep.class, 91),
    COW("cow", Cow.class, 92),
    CHICKEN("chicken", Chicken.class, 93),
    SQUID("squid", Squid.class, 94),
    WOLF("wolf", Wolf.class, 95),
    MOOSHROOM("mooshroom", MushroomCow.class, 96),
    SNOW_GOLEM("snow_golem", Snowman.class, 97),
    OCELOT("ocelot", Ocelot.class, 98),
    IRON_GOLEM("iron_golem", IronGolem.class, 99),
    HORSE("horse", Horse.class, 100),
    RABBIT("rabbit", Rabbit.class, 101),
    POLAR_BEAR("polar_bear", PolarBear.class, 102),
    LLAMA("llama", Llama.class, 103),
    LLAMA_SPIT("llama_spit", LlamaSpit.class, 104),
    PARROT("parrot", Parrot.class, 105),
    VILLAGER("villager", Villager.class, 120),
    END_CRYSTAL("end_crystal", EnderCrystal.class, 200),
    TURTLE("turtle", Turtle.class, -1),
    PHANTOM("phantom", Phantom.class, -1),
    TRIDENT("trident", Trident.class, -1),
    COD("cod", Cod.class, -1),
    SALMON("salmon", Salmon.class, -1),
    PUFFERFISH("pufferfish", PufferFish.class, -1),
    TROPICAL_FISH("tropical_fish", TropicalFish.class, -1),
    DROWNED("drowned", Drowned.class, -1),
    DOLPHIN("dolphin", Dolphin.class, -1),
    CAT("cat", Cat.class, -1),
    PANDA("panda", Panda.class, -1),
    PILLAGER("pillager", Pillager.class, -1),
    RAVAGER("ravager", Ravager.class, -1),
    TRADER_LLAMA("trader_llama", TraderLlama.class, -1),
    WANDERING_TRADER("wandering_trader", WanderingTrader.class, -1),
    FOX("fox", Fox.class, -1),
    BEE("bee", Bee.class, -1),
    HOGLIN("hoglin", Hoglin.class, -1),
    PIGLIN("piglin", Piglin.class, -1),
    STRIDER("strider", Strider.class, -1),
    ZOGLIN("zoglin", Zoglin.class, -1),
    PIGLIN_BRUTE("piglin_brute", PiglinBrute.class, -1),
    AXOLOTL("axolotl", Axolotl.class, -1),
    GLOW_ITEM_FRAME("glow_item_frame", GlowItemFrame.class, -1),
    GLOW_SQUID("glow_squid", GlowSquid.class, -1),
    GOAT("goat", Goat.class, -1),
    MARKER("marker", Marker.class, -1),
    ALLAY("allay", Allay.class, -1),
    CHEST_BOAT("chest_boat", ChestBoat.class, -1),
    FROG("frog", Frog.class, -1),
    TADPOLE("tadpole", Tadpole.class, -1),
    WARDEN("warden", Warden.class, -1),
    CAMEL("camel", Camel.class, -1),
    BLOCK_DISPLAY("block_display", BlockDisplay.class, -1),
    INTERACTION("interaction", Interaction.class, -1),
    ITEM_DISPLAY("item_display", ItemDisplay.class, -1),
    SNIFFER("sniffer", Sniffer.class, -1),
    TEXT_DISPLAY("text_display", TextDisplay.class, -1),
    BREEZE("breeze", Breeze.class, -1),
    WIND_CHARGE("wind_charge", WindCharge.class, -1),
    BREEZE_WIND_CHARGE("breeze_wind_charge", BreezeWindCharge.class, -1),
    ARMADILLO("armadillo", Armadillo.class, -1),
    BOGGED("bogged", Bogged.class, -1),
    OMINOUS_ITEM_SPAWNER("ominous_item_spawner", OminousItemSpawner.class, -1),
    /**
     * A fishing line and bobber.
     */
    FISHING_BOBBER("fishing_bobber", FishHook.class, -1, false),
    /**
     * A bolt of lightning.
     * <p>
     * Spawn with {@link World#strikeLightning(Location)}.
     */
    LIGHTNING_BOLT("lightning_bolt", LightningStrike.class, -1),
    PLAYER("player", Player.class, -1, false),
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
    @Deprecated
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
    @Deprecated
    public short getTypeId() {
        return typeId;
    }

    /**
     * Gets an entity type from its name.
     *
     * @param name the entity type's name
     * @return the matching entity type or null
     * @deprecated Magic value
     */
    @Deprecated
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
    @Deprecated
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
    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    /**
     * Gets if this EntityType is enabled by feature in a world.
     *
     * @param world the world to check
     * @return true if this EntityType can be used to spawn an Entity for this World.
     */
    public boolean isEnabledByFeature(@NotNull World world) {
        return Bukkit.getDataPackManager().isEnabledByFeature(this, world);
    }
}
