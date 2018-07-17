package io.papermc.paper.tag;

import org.bukkit.NamespacedKey;

import static org.bukkit.entity.EntityType.*;

/**
 * All tags in this class are unmodifiable, attempting to modify them will throw an
 * {@link UnsupportedOperationException}.
 */
public class EntityTags {

    private static NamespacedKey keyFor(String key) {
        //noinspection deprecation
        return new NamespacedKey("paper", key + "_settag");
    }

    /**
     * Covers undead mobs
     * @see <a href="https://minecraft.wiki/wiki/Mob#Undead_mobs">https://minecraft.wiki/wiki/Mob#Undead_mobs</a>
     */
    public static final EntitySetTag UNDEADS = new EntitySetTag(keyFor("undeads"))
        .add(DROWNED, HUSK, PHANTOM, SKELETON, SKELETON_HORSE, STRAY, WITHER, WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN, BOGGED)
        .ensureSize("UNDEADS", 14).lock();

    /**
     * Covers all horses
     */
    public static final EntitySetTag HORSES = new EntitySetTag(keyFor("horses"))
        .contains("HORSE")
        .ensureSize("HORSES", 3).lock();

    /**
     * Covers all minecarts
     */
    public static final EntitySetTag MINECARTS = new EntitySetTag(keyFor("minecarts"))
        .contains("MINECART")
        .ensureSize("MINECARTS", 7).lock();

    /**
     * Covers mobs that split into smaller mobs
     */
    public static final EntitySetTag SPLITTING_MOBS = new EntitySetTag(keyFor("splitting_mobs"))
        .add(SLIME, MAGMA_CUBE)
        .ensureSize("SLIMES", 2).lock();

    /**
     * Covers all water based mobs
     * @see <a href="https://minecraft.wiki/wiki/Mob#Aquatic_mobs">https://minecraft.wiki/wiki/Mob#Aquatic_mobs</a>
     * @deprecated in favour of {@link org.bukkit.Tag#ENTITY_TYPES_AQUATIC}
     */
    @Deprecated
    public static final EntitySetTag WATER_BASED = new EntitySetTag(keyFor("water_based"))
        .add(AXOLOTL, DOLPHIN, SQUID, GLOW_SQUID, GUARDIAN, ELDER_GUARDIAN, TURTLE, COD, SALMON, PUFFERFISH, TROPICAL_FISH, TADPOLE)
        .ensureSize("WATER_BASED", 12).lock();
}
