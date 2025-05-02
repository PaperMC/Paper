package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Salmon;

public class CraftSalmon extends io.papermc.paper.entity.PaperSchoolableFish implements Salmon { // Paper - Schooling Fish API

    public CraftSalmon(CraftServer server, net.minecraft.world.entity.animal.Salmon entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Salmon getHandle() {
        return (net.minecraft.world.entity.animal.Salmon) this.entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.Salmon.Variant.values()[variant.ordinal()]);
    }
}
