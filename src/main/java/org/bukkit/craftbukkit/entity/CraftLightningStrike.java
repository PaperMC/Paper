package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWeatherLighting;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final EntityWeatherLighting entity) {
        super(server, entity);
    }

    public boolean isEffect() {
        return ((EntityWeatherLighting) super.getHandle()).isEffect;
    }

    @Override
    public EntityWeatherLighting getHandle() {
        return (EntityWeatherLighting) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}
