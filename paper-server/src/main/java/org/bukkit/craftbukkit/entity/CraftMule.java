package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.horse.EntityHorseMule;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Mule;

public class CraftMule extends CraftChestedHorse implements Mule {

    public CraftMule(CraftServer server, EntityHorseMule entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMule";
    }

    @Override
    public Variant getVariant() {
        return Variant.MULE;
    }
}
