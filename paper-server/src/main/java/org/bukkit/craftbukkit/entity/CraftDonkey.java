package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.horse.EntityHorseDonkey;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Horse.Variant;

public class CraftDonkey extends CraftChestedHorse implements Donkey {

    public CraftDonkey(CraftServer server, EntityHorseDonkey entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDonkey";
    }

    @Override
    public Variant getVariant() {
        return Variant.DONKEY;
    }
}
