package org.bukkit.entity;

import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a villager NPC
 */
public interface Villager extends AbstractVillager {

    /**
     * Gets the current profession of this villager.
     *
     * @return Current profession.
     */
    @NotNull
    public Profession getProfession();

    /**
     * Sets the new profession of this villager.
     *
     * @param profession New profession.
     */
    public void setProfession(@NotNull Profession profession);

    /**
     * Gets the current type of this villager.
     *
     * @return Current type.
     */
    @NotNull
    public Type getVillagerType();

    /**
     * Sets the new type of this villager.
     *
     * @param type New type.
     */
    public void setVillagerType(@NotNull Type type);

    /**
     * Represents Villager type, usually corresponding to what biome they spawn
     * in.
     */
    public enum Type implements Keyed {

        DESERT,
        JUNGLE,
        PLAINS,
        SAVANNA,
        SNOWY,
        SWAMP,
        TAIGA;
        private final NamespacedKey key;

        private Type() {
            this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return key;
        }
    }

    /**
     * Represents the various different Villager professions there may be.
     * Villagers have different trading options depending on their profession,
     */
    public enum Profession implements Keyed {
        NONE,
        /**
         * Armorer profession. Wears a black apron.
         * Armorers primarily trade for iron armor, chainmail armor, and
         * sometimes diamond armor.
         */
        ARMORER,
        /**
         * Butcher profession. Wears a white apron.
         * Butchers primarily trade for raw and cooked food.
         */
        BUTCHER,
        /**
         * Cartographer profession. Wears a white robe.
         * Cartographers primarily trade for explorer maps and some paper.
         */
        CARTOGRAPHER,
        /**
         * Cleric profession. Wears a purple robe.
         * Clerics primarily trade for rotten flesh, gold ingot, redstone,
         * lapis, ender pearl, glowstone, and bottle o' enchanting.
         */
        CLERIC,
        /**
         * Farmer profession. Wears a brown robe.
         * Farmers primarily trade for food-related items.
         */
        FARMER,
        /**
         * Fisherman profession. Wears a brown robe.
         * Fisherman primarily trade for fish, as well as possibly selling
         * string and/or coal.
         */
        FISHERMAN,
        /**
         * Fletcher profession. Wears a brown robe.
         * Fletchers primarily trade for string, bows, and arrows.
         */
        FLETCHER,
        /**
         * Leatherworker profession. Wears a white apron.
         * Leatherworkers primarily trade for leather, and leather armor, as
         * well as saddles.
         */
        LEATHERWORKER,
        /**
         * Librarian profession. Wears a white robe.
         * Librarians primarily trade for paper, books, and enchanted books.
         */
        LIBRARIAN,
        /**
         * Mason profession.
         */
        MASON,
        /**
         * Nitwit profession. Wears a green apron, cannot trade.
         * Nitwit villagers do not do anything. They do not have any trades by
         * default.
         */
        NITWIT,
        /**
         * Sheperd profession. Wears a brown robe.
         * Shepherds primarily trade for wool items, and shears.
         */
        SHEPHERD,
        /**
         * Toolsmith profession. Wears a black apron.
         * Tool smiths primarily trade for iron and diamond tools.
         */
        TOOLSMITH,
        /**
         * Weaponsmith profession. Wears a black apron.
         * Weapon smiths primarily trade for iron and diamond weapons, sometimes
         * enchanted.
         */
        WEAPONSMITH;
        private final NamespacedKey key;

        private Profession() {
            this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return key;
        }
    }
}
