package io.papermc.paper.loot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

/**
 * Implementation of LootContext.
 */
class LootContextImpl implements LootContext {
    
    private final Location location;
    private final float luck;
    private final Entity lootedEntity;
    private final HumanEntity killer;

    LootContextImpl(
            Location location,
            float luck,
            Entity lootedEntity,
            HumanEntity killer
    ) {
        this.location = location.clone();
        this.luck = luck;
        this.lootedEntity = lootedEntity;
        this.killer = killer;
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public float getLuck() {
        return luck;
    }

    @Override
    public Entity getLootedEntity() {
        return lootedEntity;
    }

    @Override
    public HumanEntity getKiller() {
        return killer;
    }

    @Override
    public String toString() {
        return "LootContextImpl{" +
                "location=" + location +
                ", luck=" + luck +
                ", lootedEntity=" + lootedEntity +
                ", killer=" + killer +
                '}';
    }
}
