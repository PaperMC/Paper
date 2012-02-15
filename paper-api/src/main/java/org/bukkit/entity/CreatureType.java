package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a type of creature.
 * @deprecated Use EntityType instead.
 */
@Deprecated
public enum CreatureType {
    // These strings MUST match the strings in nms.EntityTypes and are case sensitive.
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
    PIG("Pig", Pig.class, 90),
    SHEEP("Sheep", Sheep.class, 91),
    COW("Cow", Cow.class, 92),
    CHICKEN("Chicken", Chicken.class, 93),
    SQUID("Squid", Squid.class, 94),
    WOLF("Wolf", Wolf.class, 95),
    MUSHROOM_COW("MushroomCow", MushroomCow.class, 96),
    SNOWMAN("SnowMan", Snowman.class, 97),
    VILLAGER("Villager", Villager.class, 120);

    private String name;
    private Class<? extends Entity> clazz;
    private short typeId;

    private static final Map<String, CreatureType> NAME_MAP = new HashMap<String, CreatureType>();
    private static final Map<Short, CreatureType> ID_MAP = new HashMap<Short, CreatureType>();

    static {
        for (CreatureType type : EnumSet.allOf(CreatureType.class)) {
            NAME_MAP.put(type.name, type);
            if (type.typeId != 0) {
                ID_MAP.put(type.typeId, type);
            }
        }
    }

    private CreatureType(String name, Class<? extends Entity> clazz, int typeId) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
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

    public static CreatureType fromName(String name) {
        return NAME_MAP.get(name);
    }

    public static CreatureType fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    @Deprecated
    public EntityType toEntityType() {
        return EntityType.fromName(getName());
    }

    public static CreatureType fromEntityType(EntityType creatureType) {
        return fromName(creatureType.getName());
    }
}