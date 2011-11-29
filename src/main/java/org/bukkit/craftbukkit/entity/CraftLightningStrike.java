package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWeatherStorm;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final EntityWeatherStorm entity) {
        super(server, entity);
    }

    public boolean isEffect() {
        return ((EntityWeatherStorm) super.getHandle()).isEffect;
    }

    @Override
    public EntityWeatherStorm getHandle() {
        return (EntityWeatherStorm) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrik";
    }
}
