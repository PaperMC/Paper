package org.bukkit.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;

public enum EntityType {

    // These strings MUST match the strings in nms.EntityTypes and are case sensitive.
    /**
     * An item resting on the ground.
     * <p>
     * Spawn with {@link World#dropItem(Location, ItemStack)} or {@link
     * World#dropItemNaturally(Location, ItemStack)}
     */
    DROPPED_ITEM("Item", Item.class, 1, false),
    /**
     * An experience orb.
     */
    EXPERIENCE_ORB("XPOrb", ExperienceOrb.class, 2),
    /**
     * A leash attached to a fencepost.
     */
    LEASH_HITCH("LeashKnot", LeashHitch.class, 8),
    /**
     * A painting on a wall.
     */
    PAINTING("Painting", Painting.class, 9),
    /**
     * An arrow projectile; may get stuck in the ground.
     */
    ARROW("Arrow", Arrow.class, 10),
    /**
     * A flying snowball.
     */
    SNOWBALL("Snowball", Snowball.class, 11),
    /**
     * A flying large fireball, as thrown by a Ghast for example.
     */
    FIREBALL("Fireball", LargeFireball.class, 12),
    /**
     * A flying small fireball, such as thrown by a Blaze or player.
     */
    SMALL_FIREBALL("SmallFireball", SmallFireball.class, 13),
    /**
     * A flying ender pearl.
     */
    ENDER_PEARL("ThrownEnderpearl", EnderPearl.class, 14),
    /**
     * An ender eye signal.
     */
    ENDER_SIGNAL("EyeOfEnderSignal", EnderSignal.class, 15),
    /**
     * A flying experience bottle.
     */
    THROWN_EXP_BOTTLE("ThrownExpBottle", ThrownExpBottle.class, 17),
    /**
     * An item frame on a wall.
     */
    ITEM_FRAME("ItemFrame", ItemFrame.class, 18),
    /**
     * A flying wither skull projectile.
     */
    WITHER_SKULL("WitherSkull", WitherSkull.class, 19),
    /**
     * Primed TNT that is about to explode.
     */
    PRIMED_TNT("PrimedTnt", TNTPrimed.class, 20),
    /**
     * A block that is going to or is about to fall.
     */
    FALLING_BLOCK("FallingSand", FallingBlock.class, 21, false),
    FIREWORK("FireworksRocketEntity", Firework.class, 22, false),
    ARMOR_STAND("ArmorStand", ArmorStand.class, 30, false),
    /**
     * @see CommandMinecart
     */
    MINECART_COMMAND("MinecartCommandBlock", CommandMinecart.class, 40),
    /**
     * A placed boat.
     */
    BOAT("Boat", Boat.class, 41),
    /**
     * @see RideableMinecart
     */
    MINECART("MinecartRideable", RideableMinecart.class, 42),
    /**
     * @see StorageMinecart
     */
    MINECART_CHEST("MinecartChest", StorageMinecart.class, 43),
    /**
     * @see PoweredMinecart
     */
    MINECART_FURNACE("MinecartFurnace", PoweredMinecart.class, 44),
    /**
     * @see ExplosiveMinecart
     */
    MINECART_TNT("MinecartTNT", ExplosiveMinecart.class, 45),
    /**
     * @see HopperMinecart
     */
    MINECART_HOPPER("MinecartHopper", HopperMinecart.class, 46),
    /**
     * @see SpawnerMinecart
     */
    MINECART_MOB_SPAWNER("MinecartMobSpawner", SpawnerMinecart.class, 47),
    CREEPER("Creeper", Creeper.class, 50),
    SKELETON("Skeleton", Skeleton.class, 51),
    SPIDER("Spider", Spider.class, 52),
    GIANT("Giant", Giant.class, 53),
    ZOMBIE("Zombie", Zombie.class, 54),
    SLIME("Slime", Slime.class, 55),
    GHAST("Ghast", Ghast.class, 56),
    PIG_ZOMBIE("PigZombie", PigZombie.class, 57),
    ENDERMAN("Enderman", Enderman.class, 58),
    CAVE_SPIDER("CaveSpider", CaveSpider.class, 59),
    SILVERFISH("Silverfish", Silverfish.class, 60),
    BLAZE("Blaze", Blaze.class, 61),
    MAGMA_CUBE("LavaSlime", MagmaCube.class, 62),
    ENDER_DRAGON("EnderDragon", EnderDragon.class, 63),
    WITHER("WitherBoss", Wither.class, 64),
    BAT("Bat", Bat.class, 65),
    WITCH("Witch", Witch.class, 66),
    ENDERMITE("Endermite", Endermite.class, 67),
    GUARDIAN("Guardian", Guardian.class, 68),
    PIG("Pig", Pig.class, 90),
    SHEEP("Sheep", Sheep.class, 91),
    COW("Cow", Cow.class, 92),
    CHICKEN("Chicken", Chicken.class, 93),
    SQUID("Squid", Squid.class, 94),
    WOLF("Wolf", Wolf.class, 95),
    MUSHROOM_COW("MushroomCow", MushroomCow.class, 96),
    SNOWMAN("SnowMan", Snowman.class, 97),
    OCELOT("Ozelot", Ocelot.class, 98),
    IRON_GOLEM("VillagerGolem", IronGolem.class, 99),
    HORSE("EntityHorse", Horse.class, 100),
    RABBIT("Rabbit", Rabbit.class, 101),
    VILLAGER("Villager", Villager.class, 120),
    ENDER_CRYSTAL("EnderCrystal", EnderCrystal.class, 200),
    // These don't have an entity ID in nms.EntityTypes.
    /**
     * A flying splash potion.
     */
    SPLASH_POTION(null, ThrownPotion.class, -1, false),
    /**
     * A flying chicken egg.
     */
    EGG(null, Egg.class, -1, false),
    /**
     * A fishing line and bobber.
     */
    FISHING_HOOK(null, Fish.class, -1, false),
    /**
     * A bolt of lightning.
     * <p>
     * Spawn with {@link World#strikeLightning(Location)}.
     */
    LIGHTNING(null, LightningStrike.class, -1, false),
    WEATHER(null, Weather.class, -1, false),
    PLAYER(null, Player.class, -1, false),
    COMPLEX_PART(null, ComplexEntityPart.class, -1, false),
    /**
     * An unknown entity without an Entity Class
     */
    UNKNOWN(null, null, -1, false);

    private String name;
    private Class<? extends Entity> clazz;
    private short typeId;
    private boolean independent, living;

    private static final Map<String, EntityType> NAME_MAP = new HashMap<String, EntityType>();
    private static final Map<Short, EntityType> ID_MAP = new HashMap<Short, EntityType>();

    static {
        for (EntityType type : values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(), type);
            }
            if (type.typeId > 0) {
                ID_MAP.put(type.typeId, type);
            }
        }
    }

    private EntityType(String name, Class<? extends Entity> clazz, int typeId) {
        this(name, clazz, typeId, true);
    }

    private EntityType(String name, Class<? extends Entity> clazz, int typeId, boolean independent) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
        this.independent = independent;
        if (clazz != null) {
            this.living = LivingEntity.class.isAssignableFrom(clazz);
        }
    }

    /**
     *
     * @return the entity type's name
     * @deprecated Magic value
     */
    @Deprecated
    public String getName() {
        return name;
    }

    public Class<? extends Entity> getEntityClass() {
        return clazz;
    }

    /**
     *
     * @return the raw type id 
     * @deprecated Magic value
     */
    @Deprecated
    public short getTypeId() {
        return typeId;
    }

    /**
     *
     * @param name the entity type's name
     * @return the matching entity type or null
     * @deprecated Magic value
     */
    @Deprecated
    public static EntityType fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    /**
     *
     * @param id the raw type id
     * @return the matching entity type or null
     * @deprecated Magic value
     */
    @Deprecated
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
}
