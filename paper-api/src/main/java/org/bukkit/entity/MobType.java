package org.bukkit.entity;

public enum MobType {
    CHICKEN("Chicken"),
    COW("Cow"),
    CREEPER("Creeper"),
    GHAST("Ghast"),
    PIG("Pig"),
    PIG_ZOMBIE("PigZombie"),
    SHEEP("Sheep"),
    SKELETON("Skeleton"),
    SPIDER("Spider"),
    ZOMBIE("Zombie");

    private String name;

    private MobType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
