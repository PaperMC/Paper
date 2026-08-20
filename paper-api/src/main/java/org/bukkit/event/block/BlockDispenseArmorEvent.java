package org.bukkit.event.block;

import org.bukkit.entity.LivingEntity;

/**
 * Called when an equippable item is dispensed from a block and equipped on a
 * nearby entity.
 * <p>
 * If this event is cancelled, the equipment will not be
 * equipped on the target entity.
 */
public interface BlockDispenseArmorEvent extends BlockDispenseEvent {


    /**
     * Get the living entity on which the armor was dispensed.
     *
     * @return the target entity
     */
    LivingEntity getTargetEntity();
}
