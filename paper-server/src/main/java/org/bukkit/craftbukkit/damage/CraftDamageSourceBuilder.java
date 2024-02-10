package org.bukkit.craftbukkit.damage;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;

public class CraftDamageSourceBuilder implements DamageSource.Builder {

    private final DamageType damageType;
    private Entity causingEntity;
    private Entity directEntity;
    private Location damageLocation;

    public CraftDamageSourceBuilder(DamageType damageType) {
        Preconditions.checkArgument(damageType != null, "DamageType cannot be null");
        this.damageType = damageType;
    }

    @Override
    public DamageSource.Builder withCausingEntity(Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        this.causingEntity = entity;
        return this;
    }

    @Override
    public DamageSource.Builder withDirectEntity(Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        this.directEntity = entity;
        return this;
    }

    @Override
    public DamageSource.Builder withDamageLocation(Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        this.damageLocation = location.clone();
        return this;
    }

    @Override
    public DamageSource build() {
        return CraftDamageSource.buildFromBukkit(damageType, causingEntity, directEntity, damageLocation);
    }
}
