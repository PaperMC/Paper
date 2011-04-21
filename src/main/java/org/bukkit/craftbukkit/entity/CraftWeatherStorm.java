
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWeatherStorm;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WeatherStorm;

public class CraftWeatherStorm extends CraftEntity implements WeatherStorm {
    public CraftWeatherStorm(final CraftServer server, final EntityWeatherStorm entity) {
        super(server, entity);
    }

    @Override
    public EntityWeatherStorm getHandle() {
        return (EntityWeatherStorm)super.getHandle();
    }
}
