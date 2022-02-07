package org.bukkit.entity;

/**
 * Represents groups of entities with shared spawn behaviors and mob caps.
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Spawn#Java_Edition_mob_cap">Minecraft Wiki</a>
 */
public enum SpawnCategory {

    /**
     * Entities related to Monsters, eg: Witch, Zombie, Creeper, etc.
     */
    MONSTER,
    /**
     * Entities related to Animals, eg: Strider, Cow, Turtle, etc.
     */
    ANIMAL,
    /**
     * Entities related to Water Animals, eg: Squid or Dolphin.
     */
    WATER_ANIMAL,
    /**
     * Entities related to Water Ambient, eg: Cod, PufferFish, Tropical Fish,
     * Salmon, etc.
     */
    WATER_AMBIENT,
    /**
     * Entities related to Water Underground, eg: Glow Squid.
     */
    WATER_UNDERGROUND_CREATURE,
    /**
     * Entities related to Ambient, eg: Bat.
     */
    AMBIENT,
    /**
     * All the Axolotl are represented by this Category.
     */
    AXOLOTL,
    /**
     * Entities not related to a mob, eg: Player, ArmorStand, Boat, etc.
     */
    MISC;
}
