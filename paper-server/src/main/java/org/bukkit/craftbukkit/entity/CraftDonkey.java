package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Horse.Variant;

public class CraftDonkey extends CraftChestedHorse implements Donkey {

    public CraftDonkey(CraftServer server, net.minecraft.world.entity.animal.horse.Donkey entity) {
        super(server, entity);
    }

    @Override
    public Variant getVariant() {
        return Variant.DONKEY;
    }
}
