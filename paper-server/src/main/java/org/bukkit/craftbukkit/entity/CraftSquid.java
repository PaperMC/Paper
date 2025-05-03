package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftAgeable implements Squid {

    public CraftSquid(CraftServer server, net.minecraft.world.entity.animal.Squid entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Squid getHandle() {
        return (net.minecraft.world.entity.animal.Squid) this.entity;
    }
}
