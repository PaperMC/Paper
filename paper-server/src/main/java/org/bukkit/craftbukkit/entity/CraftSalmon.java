package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Salmon;

public class CraftSalmon extends CraftFish implements Salmon {

    public CraftSalmon(CraftServer server, net.minecraft.world.entity.animal.Salmon entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Salmon getHandle() {
        return (net.minecraft.world.entity.animal.Salmon) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSalmon";
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.Salmon.Variant.values()[variant.ordinal()]);
    }
}
