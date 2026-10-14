package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cod;

public class CraftCod extends io.papermc.paper.entity.PaperSchoolableFish implements Cod { // Paper - School Fish API

    public CraftCod(CraftServer server, net.minecraft.world.entity.animal.fish.Cod entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.fish.Cod getHandle() {
        return (net.minecraft.world.entity.animal.fish.Cod) this.entity;
    }
}
