
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWeather;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Weather;

public class CraftWeather extends CraftEntity implements Weather {
    public CraftWeather(final CraftServer server, final EntityWeather entity) {
        super(server, entity);
    }

    @Override
    public EntityWeather getHandle() {
        return (EntityWeather) super.getHandle();
    }
}
