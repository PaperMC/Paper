package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CreatureType {
    CHICKEN("Chicken", Chicken.class),
    COW("Cow", Cow.class),
    CREEPER("Creeper", Creeper.class),
    GHAST("Ghast", Ghast.class),
    GIANT("Giant", Giant.class),
    MONSTER("Monster", Monster.class),
    PIG("Pig", Pig.class),
    PIG_ZOMBIE("PigZombie", PigZombie.class),
    SHEEP("Sheep", Sheep.class),
    SKELETON("Skeleton", Skeleton.class),
    SLIME("Slime", Slime.class),
    SPIDER("Spider", Spider.class),
    SQUID("Squid", Squid.class),
    ZOMBIE("Zombie", Zombie.class),
    WOLF("Wolf", Wolf.class),
    CAVE_SPIDER("CaveSpider", CaveSpider.class),
    ENDERMAN("Enderman", Enderman.class),
    SILVERFISH("Silverfish", Silverfish.class),
    ENDER_DRAGON("EnderDragon", EnderDragon.class),
    VILLAGER("Villager", Villager.class),
    BLAZE("Blaze", Blaze.class),
    MUSHROOM_COW("MushroomCow", MushroomCow.class),
    MAGMA_CUBE("MagmaCube", MagmaCube.class),
    SNOWMAN("Snowman", Snowman.class);

    private String name;
    private Class<? extends Entity> clazz;

    private static final Map<String, CreatureType> mapping = new HashMap<String, CreatureType>();

    static {
        for (CreatureType type : EnumSet.allOf(CreatureType.class)) {
            mapping.put(type.name, type);
        }
    }

    private CreatureType(String name, Class<? extends Entity> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }
    
    public Class<? extends Entity> getEntityClass() {
        return clazz;
    }

    public static CreatureType fromName(String name) {
        return mapping.get(name);
    }
}
