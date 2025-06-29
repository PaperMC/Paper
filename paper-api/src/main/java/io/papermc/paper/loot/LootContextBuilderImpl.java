package io.papermc.paper.loot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

/**
 * Implementation of LootContextBuilder.
 */
class LootContextBuilderImpl implements LootContextBuilder {
    
    private Location location;
    private float luck = 0.0f;
    private Entity lootedEntity;
    private HumanEntity killer;

    private LootContextBuilderImpl(Location location) {
        this.location = location.clone();
    }

    static LootContextBuilder create(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (location.getWorld() == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        
        // Try to use server implementation if available
        try {
            Class<?> serverImplClass = Class.forName("io.papermc.paper.loot.PaperLootContextBuilderImpl");
            return (LootContextBuilder) serverImplClass.getConstructor(Location.class).newInstance(location);
        } catch (Exception e) {
            // Fall back to API implementation
            return new LootContextBuilderImpl(location);
        }
    }

    @Override
    public LootContextBuilder location(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (location.getWorld() == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        this.location = location.clone();
        return this;
    }

    @Override
    public LootContextBuilder luck(float luck) {
        this.luck = luck;
        return this;
    }

    @Override
    public LootContextBuilder lootedEntity(Entity entity) {
        this.lootedEntity = entity;
        return this;
    }

    @Override
    public LootContextBuilder killer(HumanEntity killer) {
        this.killer = killer;
        return this;
    }

    @Override
    public LootContext build() {
        if (location == null) {
            throw new IllegalArgumentException("Location must be set");
        }
        return new LootContextImpl(
            location,
            luck,
            lootedEntity,
            killer
        );
    }
}
