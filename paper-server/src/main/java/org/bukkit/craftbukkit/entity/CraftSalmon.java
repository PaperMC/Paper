package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.EntitySalmon;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Salmon;

public class CraftSalmon extends CraftFish implements Salmon {

    public CraftSalmon(CraftServer server, EntitySalmon entity) {
        super(server, entity);
    }

    @Override
    public EntitySalmon getHandle() {
        return (EntitySalmon) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSalmon";
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(EntitySalmon.a.values()[variant.ordinal()]);
    }
}
