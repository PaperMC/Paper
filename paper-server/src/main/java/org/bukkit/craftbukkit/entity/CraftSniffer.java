package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sniffer;

public class CraftSniffer extends CraftAnimals implements Sniffer {

    public CraftSniffer(CraftServer server, net.minecraft.world.entity.animal.sniffer.Sniffer entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.sniffer.Sniffer getHandle() {
        return (net.minecraft.world.entity.animal.sniffer.Sniffer) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSniffer";
    }

    @Override
    public EntityType getType() {
        return EntityType.SNIFFER;
    }
}
