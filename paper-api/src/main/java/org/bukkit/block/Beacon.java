package org.bukkit.block;

import java.util.Collection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;

/**
 * Represents a beacon.
 */
public interface Beacon extends BlockState, InventoryHolder {

    /**
     * Returns the list of players within the beacon's range of effect.
     *
     * @return the players in range
     */
    Collection<LivingEntity> getEntitiesInRange();

    /**
     * Returns the tier of the beacon pyramid (0-4). The tier refers to the
     * beacon's power level, based on how many layers of blocks are in the
     * pyramid. Tier 1 refers to a beacon with one layer of 9 blocks under it.
     *
     * @return the beacon tier
     */
    int getTier();

    /**
     * Returns the primary effect set on the beacon
     *
     * @return the primary effect
     */
    PotionEffect getPrimaryEffect();

    /**
     * Returns the secondary effect set on the beacon.
     *
     * @return the secondary effect or null if no secondary effect
     */
    PotionEffect getSecondaryEffect();
}
