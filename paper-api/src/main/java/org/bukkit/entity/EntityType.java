package org.bukkit.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

public enum EntityType {
    // These strings MUST match the strings in nms.EntityTypes and are case sensitive.
    DROPPED_ITEM("Item", Item.class, 1, false),
    EXPERIENCE_ORB("XPOrb", ExperienceOrb.class, 2),
    PAINTING("Painting", Painting.class, 9),
    ARROW("Arrow", Arrow.class, 10),
    SNOWBALL("Snowball", Snowball.class, 11),
    FIREBALL("Fireball", LargeFireball.class, 12),
    SMALL_FIREBALL("SmallFireball", SmallFireball.class, 13),
    ENDER_PEARL("ThrownEnderpearl", EnderPearl.class, 14),
    ENDER_SIGNAL("EyeOfEnderSignal", EnderSignal.class, 15),
    THROWN_EXP_BOTTLE("ThrownExpBottle", ThrownExpBottle.class, 17),
    ITEM_FRAME("ItemFrame", ItemFrame.class, 18),
    WITHER_SKULL("WitherSkull", WitherSkull.class, 19),
    PRIMED_TNT("PrimedTnt", TNTPrimed.class, 20),
    FALLING_BLOCK("FallingSand", FallingBlock.class, 21, false),
    FIREWORK("FireworksRocketEntity", Firework.class, 22, false),
    MINECART("Minecart", Minecart.class, 40),
    BOAT("Boat", Boat.class, 41),
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
    VILLAGER("Villager", Villager.class, 120),
    ENDER_CRYSTAL("EnderCrystal", EnderCrystal.class, 200),
    // These don't have an entity ID in nms.EntityTypes.
    SPLASH_POTION(null, ThrownPotion.class, -1, false),
    EGG(null, Egg.class, -1, false),
    FISHING_HOOK(null, Fish.class, -1, false),
    /**
     * Spawn with {@link World#strikeLightning(org.bukkit.Location)}.
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

    public String getName() {
        return name;
    }

    public Class<? extends Entity> getEntityClass() {
        return clazz;
    }

    public short getTypeId() {
        return typeId;
    }

    public static EntityType fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static EntityType fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    /**
     * Some entities cannot be spawned using {@link World#spawnCreature(Location, EntityType)}
     * or {@link World#spawn(Location, Class)}, usually
     * because they require additional information in order to spawn.
     * @return False if the entity type cannot be spawned
     */
    public boolean isSpawnable() {
        return independent;
    }

    public boolean isAlive() {
        return living;
    }
}
