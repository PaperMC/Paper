package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Parrot;

public class CraftParrot extends CraftTameableAnimal implements Parrot {

    public CraftParrot(CraftServer server, net.minecraft.world.entity.animal.Parrot parrot) {
        super(server, parrot);
    }

    @Override
    public net.minecraft.world.entity.animal.Parrot getHandle() {
        return (net.minecraft.world.entity.animal.Parrot) this.entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.Parrot.Variant.byId(variant.ordinal()));
    }

    @Override
    public boolean isDancing() {
        return this.getHandle().isPartyParrot();
    }
}
