package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cod;

public class CraftCod extends CraftFish implements Cod {

    public CraftCod(CraftServer server, net.minecraft.world.entity.animal.Cod entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cod getHandle() {
        return (net.minecraft.world.entity.animal.Cod) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCod";
    }
}
