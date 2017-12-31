package org.bukkit.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Merchant;

/**
 * Represents a villager NPC
 */
public interface Villager extends Ageable, NPC, InventoryHolder, Merchant {

    /**
     * Gets the current profession of this villager.
     *
     * @return Current profession.
     */
    public Profession getProfession();

    /**
     * Sets the new profession of this villager.
     *
     * @param profession New profession.
     */
    public void setProfession(Profession profession);

    /**
     * Get the current {@link Career} for this Villager.
     *
     * @return the {@link Career}
     */
    public Career getCareer();

    /**
     * Set the new {@link Career} for this Villager.
     * This method will reset the villager's trades to the new career.
     *
     * @param career the new career, or null to clear the career to a random one
     * @throws IllegalArgumentException when the new {@link Career} cannot be
     * used with this Villager's current {@link Profession}.
     */
    public void setCareer(Career career);

    /**
     * Set the new {@link Career} for this Villager.
     *
     * @param career the new career, or null to clear the career to a random one
     * @param resetTrades true to reset this Villager's trades to the new
     * career's (if any)
     * @throws IllegalArgumentException when the new {@link Career} cannot be
     * used with this Villager's current {@link Profession}.
     */
    public void setCareer(Career career, boolean resetTrades);

    /**
     * Gets this villager's inventory.
     * <br>
     * Note that this inventory is not the Merchant inventory, rather, it is the
     * items that a villager might have collected (from harvesting crops, etc.)
     *
     * {@inheritDoc}
     */
    @Override
    Inventory getInventory();

    /**
     * Gets this villager's riches, the number of emeralds this villager has
     * been given.
     *
     * @return the villager's riches
     */
    int getRiches();

    /**
     * Sets this villager's riches.
     *
     * @see Villager#getRiches()
     *
     * @param riches the new riches
     */
    void setRiches(int riches);

    /**
     * Represents the various different Villager professions there may be.
     * Villagers have different trading options depending on their profession,
     */
    public enum Profession {
        /**
         * Normal. <b>Reserved for Zombies.</b>
         * @deprecated Unused
         */
        @Deprecated
        NORMAL(true),
        /**
         * Farmer profession. Wears a brown robe.
         */
        FARMER(false),
        /**
         * Librarian profession. Wears a white robe.
         */
        LIBRARIAN(false),
        /**
         * Priest profession. Wears a purple robe.
         */
        PRIEST(false),
        /**
         * Blacksmith profession. Wears a black apron.
         */
        BLACKSMITH(false),
        /**
         * Butcher profession. Wears a white apron.
         */
        BUTCHER(false),
        /**
         * Nitwit profession. Wears a green apron, cannot trade.
         */
        NITWIT(false),
        /**
         * Husk. <b>Reserved for Zombies</b>
         * @deprecated Unused
         */
        @Deprecated
        HUSK(true);
        private final boolean zombie;

        private Profession(boolean zombie) {
            this.zombie = zombie;
        }

        /**
         * Returns if this profession can only be used by zombies.
         *
         * @return zombie profession status
         * @deprecated Unused
         */
        @Deprecated
        public boolean isZombie() {
            return zombie;
        }

        /**
         * Get an immutable list of {@link Career} belonging to this Profession.
         *
         * @return an immutable list of careers for this profession, or an empty
         * map if this Profession has no careers.
         */
        public List<Career> getCareers() {
            return Career.getCareers(this);
        }
    }

    /**
     * The Career of this Villager.
     * Each {@link Profession} has a set of careers it is applicable to. Each
     * career dictates the trading options that are generated.
     */
    public enum Career {
        /*
        NOTE: The Career entries are order-specific. They should be maintained in the numerical order they are used in the CB implementation.
        (e.g. Farmer careers are 1,2,3,4 so Career should reflect that numerical order in their ordinal status)
         */
        // Farmer careers
        /**
         * Farmers primarily trade for food-related items.
         */
        FARMER(Profession.FARMER),
        /**
         * Fisherman primarily trade for fish, as well as possibly selling
         * string and/or coal.
         */
        FISHERMAN(Profession.FARMER),
        /**
         * Shepherds primarily trade for wool items, and shears.
         */
        SHEPHERD(Profession.FARMER),
        /**
         * Fletchers primarily trade for string, bows, and arrows.
         */
        FLETCHER(Profession.FARMER),
        // Librarian careers
        /**
         * Librarians primarily trade for paper, books, and enchanted books.
         */
        LIBRARIAN(Profession.LIBRARIAN),
        /**
         * Cartographers primarily trade for explorer maps and some paper.
         */
        CARTOGRAPHER(Profession.LIBRARIAN),
        // Priest careers
        /**
         * Clerics primarily trade for rotten flesh, gold ingot, redstone,
         * lapis, ender pearl, glowstone, and bottle o' enchanting.
         */
        CLERIC(Profession.PRIEST),
        // Blacksmith careers
        /**
         * Armorers primarily trade for iron armor, chainmail armor, and
         * sometimes diamond armor.
         */
        ARMORER(Profession.BLACKSMITH),
        /**
         * Weapon smiths primarily trade for iron and diamond weapons, sometimes
         * enchanted.
         */
        WEAPON_SMITH(Profession.BLACKSMITH),
        /**
         * Tool smiths primarily trade for iron and diamond tools.
         */
        TOOL_SMITH(Profession.BLACKSMITH),
        // Butcher careers
        /**
         * Butchers primarily trade for raw and cooked food.
         */
        BUTCHER(Profession.BUTCHER),
        /**
         * Leatherworkers primarily trade for leather, and leather armor, as
         * well as saddles.
         */
        LEATHERWORKER(Profession.BUTCHER),
        // Nitwit
        /**
         * Nitwit villagers do not do anything. They do not have any trades by
         * default.
         */
        NITWIT(Profession.NITWIT);

        private static final Multimap<Profession, Career> careerMap = LinkedListMultimap.create();
        private final Profession profession;

        private Career(Profession profession) {
            this.profession = profession;
        }

        /**
         * Get the {@link Profession} this {@link Career} belongs to.
         *
         * @return the {@link Profession}.
         */
        public Profession getProfession() {
            return profession;
        }

        /**
         * Get an immutable list of {@link Career}s that can be used with a
         * given {@link Profession}
         * 
         * @param profession the profession to get careers for
         * @return an immutable list of Careers that can be used by a
         * profession, or an empty map if the profession was not found
         */
        public static List<Career> getCareers(Profession profession) {
            return careerMap.containsKey(profession) ? ImmutableList.copyOf(careerMap.get(profession)) : ImmutableList.<Career>of();
        }

        static {
            for (Career career : Career.values()) {
                careerMap.put(career.profession, career);
            }
        }
    }
}
