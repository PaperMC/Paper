package org.bukkit.event.entity;

import io.papermc.paper.event.entity.EntityDyeEvent;
import org.bukkit.entity.Sheep;

/**
 * Called when a sheep's wool is dyed
 */
public interface SheepDyeWoolEvent extends EntityDyeEvent {

    @Override
    Sheep getEntity();
}
