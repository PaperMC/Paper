package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Mule;

public class CraftMule extends CraftChestedHorse implements Mule {

    public CraftMule(CraftServer server, net.minecraft.world.entity.animal.horse.Mule entity) {
        super(server, entity);
    }

    @Override
    public Variant getVariant() {
        return Variant.MULE;
    }
}
