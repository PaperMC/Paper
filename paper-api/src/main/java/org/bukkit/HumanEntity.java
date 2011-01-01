
package org.bukkit;

/**
 * Represents a human entity, such as an NPC or a player
 */
public interface HumanEntity extends LivingEntity {
    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    public String getName();

    /**
     * Gets the item this entity has currently selected, which will be shown in
     * their hand
     *
     * @return ItemStack containing details on the item this entity has selected
     */
    public ItemStack getSelectedItem();
}
