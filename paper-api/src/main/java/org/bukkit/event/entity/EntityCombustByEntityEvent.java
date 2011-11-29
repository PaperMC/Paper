package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

public class EntityCombustByEntityEvent extends EntityCombustEvent {

    private Entity combuster;

    public EntityCombustByEntityEvent(Entity combuster, Entity combustee, int duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    /**
     * The combuster can be a WeatherStorm a Blaze, or an Entity holding a FIRE_ASPECT enchanted item.
     * @return the Entity that set the combustee alight.
     */
    public Entity getCombuster() {
        return combuster;
    }
}
