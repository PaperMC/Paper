package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityThrownTrident;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Trident;

public class CraftTrident extends CraftArrow implements Trident {

    public CraftTrident(CraftServer server, EntityThrownTrident entity) {
        super(server, entity);
    }

    @Override
    public EntityThrownTrident getHandle() {
        return (EntityThrownTrident) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTrident";
    }

    @Override
    public EntityType getType() {
        return EntityType.TRIDENT;
    }
}
