package org.bukkit.entity;

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
         */
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
         */
        HUSK(true);
        private final boolean zombie;

        private Profession(boolean zombie) {
            this.zombie = zombie;
        }

        /**
         * Returns if this profession can only be used by zombies.
         *
         * @return zombie profession status
         */
        public boolean isZombie() {
            return zombie;
        }
    }
}
